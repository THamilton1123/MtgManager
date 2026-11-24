package com.magMan.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.magMan.scryfall.ScryfallSearchCriteria;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScryfallDaoTest {

    private static final ScryfallDao dao = new ScryfallDao();

    // =========================================================================
    // 1. OFFLINE UNIT TESTS (Instant, 0 API requests, tests query permutations)
    // =========================================================================

    @Test
    void buildQueryString_nullOrEmptyCriteriaReturnsEmptyString() {
        assertEquals("", dao.buildQueryString(null));
        assertEquals("", dao.buildQueryString(new ScryfallSearchCriteria()));
    }

    @Test
    void buildQueryString_exactName() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setName("Propaganda");
        criteria.setExactName(true);

        assertEquals("!\"Propaganda\" not:digital", dao.buildQueryString(criteria));
    }

    @Test
    void buildQueryString_partialNameAndEscapedQuotes() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setName("Urza");
        criteria.setExactName(false);
        criteria.setOracleText("Draw \"two\" cards");

        assertEquals("Urza fo:\"Draw \\\"two\\\" cards\" not:digital", dao.buildQueryString(criteria));
    }

    @Test
    void buildQueryString_typesAndNumericOperators() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setSuperType("Legendary");
        criteria.setType("Creature");
        criteria.setSubType("Human Wizard");
        criteria.setCmcOperator("<=");
        criteria.setCmc(4);
        criteria.setPowerOperator(">=");
        criteria.setPower("2");
        criteria.setToughnessOperator("=");
        criteria.setToughness("3");
        criteria.setLoyaltyOperator(">");
        criteria.setLoyalty("3");

        String expected = "t:Legendary t:Creature t:Human t:Wizard cmc<=4 pow>=2 tou=3 loy>3 not:digital";
        assertEquals(expected, dao.buildQueryString(criteria));
    }

    @Test
    void buildQueryString_colorModesAndManaCost() {
        // Exact colors + mana cost
        ScryfallSearchCriteria exactCriteria = new ScryfallSearchCriteria();
        exactCriteria.setColors(List.of("u", "r"));
        exactCriteria.setColorSearchType("Exactly");
        exactCriteria.setManaCost("{1}{U}{R}");

        assertEquals("m:{1}{U}{R} c=UR not:digital", dao.buildQueryString(exactCriteria));

        // Exclude colors + no mana cost
        ScryfallSearchCriteria excludeCriteria = new ScryfallSearchCriteria();
        excludeCriteria.setColors(List.of("B", "G"));
        excludeCriteria.setColorSearchType("Exclude");
        excludeCriteria.setNoManaCost(true);

        assertEquals("-has:manacost -c:BG not:digital", dao.buildQueryString(excludeCriteria));
    }

    @Test
    void buildQueryString_colorIdentityModes() {
        ScryfallSearchCriteria exactIdentity = new ScryfallSearchCriteria();
        exactIdentity.setName("Kenrith");
        exactIdentity.setColorIdentity(List.of("W", "U", "B", "R", "G"));
        exactIdentity.setColorIdentitySearchType("Exactly");

        assertEquals("Kenrith identity=WUBRG not:digital", dao.buildQueryString(exactIdentity));

        ScryfallSearchCriteria withinIdentity = new ScryfallSearchCriteria();
        withinIdentity.setColorIdentity(List.of("W", "U"));
        withinIdentity.setColorIdentitySearchType("Within");

        assertEquals("identity<=WU not:digital", dao.buildQueryString(withinIdentity));
    }

    @Test
    void buildQueryString_formatLegalitySetRarityFlavorArtistLanguage() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setFormat("Commander");
        criteria.setLegality("Not Legal");
        criteria.setSetCode("lea");
        criteria.setRarity("Rare");
        criteria.setFlavorText("In the beginning");
        criteria.setArtistName("Christopher Rush");
        criteria.setLanguage("en");

        String expected = "s:lea r:rare ft:\"In the beginning\" a:\"Christopher Rush\" lang:en -legal:commander not:digital";
        assertEquals(expected, dao.buildQueryString(criteria));
    }

    // =========================================================================
    // 2. LIVE API TESTS (Rate-limited automatically at >= 550ms per request)
    // =========================================================================

    @Test
    void searchScryfall_exactNameAllPrintingsReturnsMultiplePrintings() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setName("Propaganda");
        criteria.setExactName(true);
        criteria.setUniqueMode("prints");

        JsonNode results = dao.searchScryfall(criteria);

        assertNotNull(results, "Expected non-null JsonNode array from Scryfall");
        assertTrue(results.isArray());
        assertTrue(results.size() >= 20, "Expected at least 20 printings of Propaganda, got: " + results.size());
        assertEquals("Propaganda", results.get(0).path("name").asText());
    }

    @Test
    void searchScryfall_uniqueCardsCollapsesPrintingsToOne() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setName("Propaganda");
        criteria.setExactName(true);
        criteria.setUniqueMode("cards"); // unique=cards

        JsonNode results = dao.searchScryfall(criteria);

        assertNotNull(results);
        assertEquals(1, results.size(), "Expected unique=cards to return exactly 1 card");
    }

    @Test
    void searchScryfall_manaCostWithCurlyBracesDoesNotThrowUriException() {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setName("Propaganda");
        criteria.setManaCost("{2}{U}");
        criteria.setUniqueMode("cards");

        JsonNode results = dao.searchScryfall(criteria);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Propaganda", results.get(0).path("name").asText());
    }

    @Test
    void getPrintingByUuid_returnsSingleCardArray() {
        // Grab a verified real UUID from a live Propaganda search
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();
        criteria.setName("Propaganda");
        criteria.setExactName(true);
        criteria.setUniqueMode("cards");

        JsonNode searchResults = dao.searchScryfall(criteria);
        assertNotNull(searchResults);
        String realUuid = searchResults.get(0).path("id").asText();

        // Now verify getPrintingByUuid retrieves that exact card
        JsonNode results = dao.getPrintingByUuid(realUuid);

        assertNotNull(results);
        assertTrue(results.isArray());
        assertEquals(1, results.size());
        assertEquals("Propaganda", results.get(0).path("name").asText());
    }

    @Test
    void searchScryfall_emptyCriteriaReturnsNullWithoutCallingApi() {
        JsonNode results = dao.searchScryfall(new ScryfallSearchCriteria());
        assertNull(results);
    }
}
