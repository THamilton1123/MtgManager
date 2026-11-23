package com.magMan.persistence;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magMan.scryfall.ScryfallSearchCriteria;
import com.magMan.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ScryfallDao implements PropertiesLoader {

    private static final long MIN_DELAY_MS = 550; // Safe for Scryfall's 2 req/sec (500ms) search limit
    private static long lastRequestTime = 0;

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final ObjectMapper mapper;
    private final Properties scryProps = loadProperties("/scryfall.properties");
    private final String SCRY_URI = scryProps.getProperty("uri");

    public ScryfallDao() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Handles ANY combination of search filters in one method.
     *
     * @param criteria
     * @return
     */
    public JsonNode searchScryfall(ScryfallSearchCriteria criteria) {
        String query = buildQueryString(criteria);
        if (query.isEmpty()) {
            return null;
        }

        String uniqueMode = criteria.isAllPrintings() ? "prints" : "cards";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SCRY_URI + "search")
                .queryParam("q", query.replace("{", "%7B").replace("}", "%7D"))
                .queryParam("include_extras", criteria.isIncludeExtras())
                .queryParam("unique", uniqueMode);

        return executeAndExtractDataNode(target);
    }

    /**
     *
     * @param uuid
     * @return
     */
    public JsonNode getPrintingByUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            return null;
        }
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SCRY_URI + uuid.trim());
        return executeAndExtractDataNode(target);
    }

    /**
     * Pure Java query builder: converts a criteria object into Scryfall syntax.
     * Package-private/public so JUnit can test permutations offline with zero API calls!
     *
     * @param criteria
     * @return
     */
    public String buildQueryString(ScryfallSearchCriteria criteria) {
        if (criteria == null) {
            return "";
        }

        List<String> terms = new ArrayList<>();

        // 1. Card Name (supports exact or partial)
        if (isPresent(criteria.getName())) {
            if (criteria.isExactName()) {
                terms.add("!\"" + escapeQuotes(criteria.getName()) + "\"");
            } else {
                terms.add(criteria.getName().trim());
            }
        }

        // 2. Set & Rarity
        if (isPresent(criteria.getSetCode())) {
            terms.add("s:" + criteria.getSetCode().trim());
        }
        if (isPresent(criteria.getRarity())) {
            terms.add("r:" + criteria.getRarity().trim().toLowerCase());
        }

        // 3. Oracle Text, Flavor Text, Artist, Language
        if (isPresent(criteria.getOracleText())) {
            terms.add("o:\"" + escapeQuotes(criteria.getOracleText()) + "\"");
        }
        if (isPresent(criteria.getFlavorText())) {
            terms.add("ft:\"" + escapeQuotes(criteria.getFlavorText()) + "\"");
        }
        if (isPresent(criteria.getArtistName())) {
            terms.add("a:\"" + escapeQuotes(criteria.getArtistName()) + "\"");
        }
        if (isPresent(criteria.getLanguage())) {
            terms.add("lang:" + criteria.getLanguage().trim().toLowerCase());
        }

        // 4. Supertype, Type, and Subtype (all map to t: in Scryfall)
        appendTypeTerms(terms, criteria.getSuperType());
        appendTypeTerms(terms, criteria.getType());
        appendTypeTerms(terms, criteria.getSubType());

        // 5. Format & Legality (e.g., legal:commander, banned:legacy, -legal:standard)
        if (isPresent(criteria.getFormat())) {
            String fmt = criteria.getFormat().trim().toLowerCase();
            String leg = isPresent(criteria.getLegality()) ? criteria.getLegality().trim().toLowerCase() : "legal";

            if ("not legal".equals(leg) || "not_legal".equals(leg)) {
                terms.add("-legal:" + fmt);
            } else {
                terms.add(leg + ":" + fmt); // "legal", "banned", or "restricted"
            }
        }

        // 6. Mana Cost & Colors
        if (criteria.isNoManaCost()) {
            terms.add("-has:manacost");
        } else if (isPresent(criteria.getManaCost())) {
            terms.add("m:" + criteria.getManaCost().trim());
        }

        if (criteria.getColors() != null && !criteria.getColors().isEmpty()) {
            String colorStr = String.join("", criteria.getColors()).toUpperCase();
            String searchType = isPresent(criteria.getColorSearchType())
                    ? criteria.getColorSearchType().trim().toLowerCase()
                    : "include";

            switch (searchType) {
                case "exactly":
                    terms.add("c=" + colorStr);
                    break;
                case "exclude":
                    terms.add("-c:" + colorStr);
                    break;
                case "within":
                    terms.add("c<=" + colorStr);
                    break;
                case "include":
                default:
                    terms.add("c:" + colorStr);
                    break;
            }
        }

        if (criteria.getColorIdentity() != null && !criteria.getColorIdentity().isEmpty()) {
            String identityStr = String.join("", criteria.getColorIdentity()).toUpperCase();
            String idSearchType = isPresent(criteria.getColorIdentitySearchType())
                    ? criteria.getColorIdentitySearchType().trim().toLowerCase()
                    : "within";

            switch (idSearchType) {
                case "exactly":
                    terms.add("identity=" + identityStr);
                    break;
                case "include":
                    terms.add("identity:" + identityStr);
                    break;
                case "exclude":
                    terms.add("-identity:" + identityStr);
                    break;
                case "within":
                default:
                    terms.add("identity<=" + identityStr);
                    break;
            }
        }

        // 7. Stats: CMC (Integer) and Power/Toughness/Loyalty (String)
        if (criteria.getCmc() != null) {
            appendStatTerm(terms, "cmc", criteria.getCmcOperator(), String.valueOf(criteria.getCmc()));
        }
        appendStatTerm(terms, "pow", criteria.getPowerOperator(), criteria.getPower());
        appendStatTerm(terms, "tou", criteria.getToughnessOperator(), criteria.getToughness());
        appendStatTerm(terms, "loy", criteria.getLoyaltyOperator(), criteria.getLoyalty());

        // 8. Exclude digital-only cards unless explicitly searching a digital set
        if (!terms.isEmpty()) {
            terms.add("not:digital");
        }

        return String.join(" ", terms);
    }

    /**
     *
     * @param target
     * @return
     */
    private JsonNode executeAndExtractDataNode(WebTarget target) {
        enforceRateLimit();
        try {
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("User-Agent", "magMan/1.0 (thamilton12; simple collection browser)")
                    .get();

            if (response.getStatus() == 200) {
                String body = response.readEntity(String.class);
                JsonNode rootNode = mapper.readTree(body);
                String objectType = rootNode.path("object").asText();

                if ("list".equals(objectType)) {
                    JsonNode dataArrayNode = rootNode.get("data");
                    if (dataArrayNode != null && dataArrayNode.isArray()) {
                        return dataArrayNode;
                    }
                } else if ("card".equals(objectType)) {
                    return mapper.createArrayNode().add(rootNode);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve or parse data from Scryfall: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     *
     */
    private synchronized void enforceRateLimit() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRequestTime;
        if (elapsed < MIN_DELAY_MS) {
            try {
                Thread.sleep(MIN_DELAY_MS - elapsed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }

    /**
     *
     * @param value
     * @return
     */
    private boolean isPresent(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     *
     * @param input
     * @return
     */
    private String escapeQuotes(String input) {
        return input.trim().replace("\"", "\\\"");
    }

    /**
     *
     * @param terms
     * @param rawTypeInput
     */
    private void appendTypeTerms(List<String> terms, String rawTypeInput) {
        if (isPresent(rawTypeInput)) {
            for (String part : rawTypeInput.trim().split("\\s+")) {
                terms.add("t:" + part);
            }
        }
    }

    /**
     *
     * @param terms
     * @param prefix
     * @param operator
     * @param value
     */
    private void appendStatTerm(List<String> terms, String prefix, String operator, String value) {
        if (isPresent(value)) {
            String op = isPresent(operator) ? operator.trim() : "=";
            terms.add(prefix + op + value.trim());
        }
    }
}
