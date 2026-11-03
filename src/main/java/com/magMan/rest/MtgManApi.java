package com.magMan.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.magMan.entity.Card;
import com.magMan.entity.Ruling;
import com.magMan.persistence.GenericDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Path("/mycards/{cardname}")
public class MtgManApi {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final Logger logger = LogManager.getLogger(this.getClass());

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media types "text/plain", "application/json", or "text/html"
    @Produces({"text/plain", "application/json", "text/html"})
    public Response getMyCardsByNameExact(@PathParam("cardname") String cardName, @Context HttpHeaders headers) {
        GenericDao<Card> cardDao = new GenericDao<>(Card.class);
        List<Card> allMyCards = cardDao.getAll();
        List<Card> retrievedCards = new ArrayList<>();
        for  (Card card : allMyCards) {
            if(card.getCardName().equals(cardName)) {
                retrievedCards.add(card);
            }
        }

        List<MediaType> acceptableMediaTypes = headers.getAcceptableMediaTypes();
        for (MediaType type : acceptableMediaTypes) {
            if (type.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                String jsonOutput = null;
                try {
                    jsonOutput = mapper.writeValueAsString(retrievedCards);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error while creating json output.  Cards being converted: {}", retrievedCards);
                }
                return Response.status(200).type(MediaType.APPLICATION_JSON).entity(jsonOutput).build();
            }
            if (type.isCompatible(MediaType.TEXT_HTML_TYPE)) {
                String htmlOutput = convertListToHtmlPage(retrievedCards);
                return Response.status(200).type(MediaType.TEXT_HTML).entity(htmlOutput).build();
            }
            if (type.isCompatible(MediaType.TEXT_PLAIN_TYPE)) {
                String textOutput = convertListToPlainText(retrievedCards);
                return Response.status(200).type(MediaType.TEXT_PLAIN).entity(textOutput).build();
            }
        }
        return Response.status(406).entity("Unsupported media type requested.  This endpoint supports text/plain, application/json, and text/html.").type("text/plain").build();
    }

    /**
     * Converts a single Card object and its nested Rulings into an HTML snippet.
     * I admit, Google wrote the code for this method for me.
     */
    public static String convertCardToHtml(Card card) {
        StringBuilder html = new StringBuilder();

        html.append("<div class=\"mtg-card\">\n");
        html.append("  <div class=\"card-header\">\n");
        html.append("    <span class=\"card-name\">").append(card.getCardName()).append("</span>\n");

        int cmc = card.getCardCmc() != null ? card.getCardCmc() : 0;
        html.append("    <span class=\"card-cmc\">CMC: ").append(cmc).append("</span>\n");
        html.append("  </div>\n");

        html.append("  <div class=\"card-meta\">\n");
        html.append("    <span class=\"card-type\">").append(card.getCardType()).append("</span>\n");
        html.append("    <span class=\"card-qty\">Qty: ").append(card.getCardQuantity()).append("</span>\n");
        html.append("  </div>\n");

        html.append("  <div class=\"card-id\">Database ID: #").append(card.getId()).append("</div>\n");

        // Process Nested Rulings
        List<Ruling> rulings = card.getRulings();
        if (rulings != null && !rulings.isEmpty()) {
            html.append("  <div class=\"rulings-section\">\n");
            html.append("    <h4>Official Rulings</h4>\n");
            html.append("    <ul>\n");

            for (Ruling ruling : rulings) {
                html.append("      <li>\n");
                if (ruling.getRulingDate() != null) {
                    String formattedDate = ruling.getRulingDate().format(DATE_FORMATTER);
                    html.append("        <span class=\"ruling-date\">[").append(formattedDate).append("]</span>\n");
                }
                String text = ruling.getRulingText() != null ? ruling.getRulingText() : "";
                html.append("        <span class=\"ruling-text\">").append(text).append("</span>\n");
                html.append("      </li>\n");
            }

            html.append("    </ul>\n");
            html.append("  </div>\n");
        } else {
            html.append("  <div class=\"no-rulings\">No rulings available for this card.</div>\n");
        }

        html.append("</div>");
        return html.toString();
    }

    /**
     * Converts a full list of Cards into a complete, responsive HTML webpage template.
     * I admit, Google wrote the code for this method for me.
     */
    public static String convertListToHtmlPage(List<Card> cardList) {
        StringBuilder page = new StringBuilder();

        // 1. HTML head section and updated styling
        page.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("  <meta charset=\"UTF-8\">\n")
                .append("  <title>Magic Collection Gallery</title>\n")
                .append("  <style>\n")
                .append("    body { font-family: 'Segoe UI', system-ui, sans-serif; background-color: #121212; color: #e0e0e0; padding: 30px; margin: 0; }\n")
                .append("    h2 { color: #fff; border-bottom: 2px solid #333; padding-bottom: 10px; }\n")
                .append("    .card-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 25px; margin-top: 20px; }\n")
                .append("    .mtg-card { background: #1e1e1e; border: 1px solid #333; border-radius: 10px; padding: 20px; box-shadow: 0 4px 8px rgba(0,0,0,0.4); display: flex; flex-direction: column; }\n")
                .append("    .card-header { display: flex; justify-content: space-between; font-weight: bold; font-size: 1.25em; margin-bottom: 6px; }\n")
                .append("    .card-name { color: #ffffff; }\n")
                .append("    .card-cmc { color: #ffb74d; }\n")
                .append("    .card-meta { display: flex; justify-content: space-between; font-style: italic; color: #b0bec5; font-size: 0.95em; margin-bottom: 4px; }\n")
                .append("    .card-id { font-size: 0.8em; color: #616161; margin-bottom: 15px; }\n")
                .append("    .rulings-section { background: #262626; padding: 12px 16px; border-radius: 6px; font-size: 0.88em; border-left: 4px solid #ffb74d; margin-top: auto; }\n")
                .append("    .rulings-section h4 { margin: 0 0 8px 0; color: #ffb74d; text-transform: uppercase; font-size: 0.75em; letter-spacing: 0.8px; }\n")
                .append("    .rulings-section ul { margin: 0; padding-left: 0; list-style: none; }\n")
                .append("    .rulings-section li { margin-bottom: 8px; line-height: 1.4; }\n")
                .append("    .rulings-section li:last-child { margin-bottom: 0; }\n")
                .append("    .ruling-date { font-weight: bold; color: #81c784; margin-right: 6px; font-variant-numeric: tabular-nums; }\n")
                .append("    .ruling-text { color: #e0e0e0; }\n")
                .append("    .no-rulings { font-size: 0.85em; color: #757575; font-style: italic; margin-top: auto; padding-top: 10px; }\n")
                .append("  </style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("  <h2>My Magic: The Gathering Collection</h2>\n")
                .append("  <div class=\"card-grid\">\n");

        // 2. Loop through every card
        for (Card card : cardList) {
            page.append(convertCardToHtml(card)).append("\n");
        }

        // 3. Document closer
        page.append("  </div>\n")
                .append("</body>\n")
                .append("</html>\n");

        return page.toString();
    }

    /**
     * Converts a full list of Cards into plain text.
     * I admit, Google wrote the code for this method for me.
     */
    public static String convertListToPlainText(List<Card> cardList) {
        StringBuilder sb = new StringBuilder();

        sb.append("==================================================\n");
        sb.append("MAGIC: THE GATHERING COLLECTION REPORT\n");
        sb.append("==================================================\n\n");

        for (Card card : cardList) {
            sb.append("Card Name: ").append(card.getCardName()).append("\n");

            int cmc = card.getCardCmc() != null ? card.getCardCmc() : 0;
            sb.append("CMC:       ").append(cmc).append("\n");
            sb.append("Type:      ").append(card.getCardType()).append("\n");
            sb.append("Quantity:  ").append(card.getCardQuantity()).append("\n");
            sb.append("DB ID:     #").append(card.getId()).append("\n");

            // Handle Nested Rulings
            List<Ruling> rulings = card.getRulings();
            if (rulings != null && !rulings.isEmpty()) {
                sb.append("Official Rulings:\n");
                for (Ruling ruling : rulings) {
                    sb.append("  - ");
                    if (ruling.getRulingDate() != null) {
                        sb.append("[").append(ruling.getRulingDate().format(DATE_FORMATTER)).append("] ");
                    }
                    sb.append(ruling.getRulingText() != null ? ruling.getRulingText() : "").append("\n");
                }
            } else {
                sb.append("Rulings:   None available.\n");
            }

            sb.append("--------------------------------------------------\n\n");
        }

        return sb.toString();
    }
}
