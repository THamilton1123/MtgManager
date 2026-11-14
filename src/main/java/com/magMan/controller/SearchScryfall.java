package com.magMan.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.magMan.entity.Card;
import com.magMan.scryfall.Printing;
import com.magMan.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * A simple servlet to browse an MTG card collection.
 * @author thamilton12
 */

@WebServlet(
        urlPatterns = {"/searchScryfall"}
)

public class SearchScryfall extends HttpServlet implements PropertiesLoader {
    private final Logger logger;
    private final Properties properties;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public SearchScryfall() {
        this.logger = LogManager.getLogger(this.getClass());
        this.properties = loadProperties("/scryfall.properties");
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cardName = req.getParameter("searchTermCardName");
        if (cardName == null || cardName.trim().isEmpty()) {
            resp.sendRedirect("index.jsp");
            return;
        }

        HttpResponse<String> scryfallResponse;
        try {
            String encodedCardName = URLEncoder.encode(cardName, StandardCharsets.UTF_8);
            String uri = properties.getProperty("uri") + "search?q=" + encodedCardName + "+not:digital+include:extras&unique=prints";

            HttpRequest scryfallRequest = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Accept", "application/json")
                    .header("User-Agent", "magMan/1.0 (thamilton12; simple collection browser)")
                    .GET()
                    .build();
            scryfallResponse = client.send(scryfallRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("API request was interrupted. {}", e.getMessage());
            throw new ServletException("API request was interrupted", e);
        } catch (Exception e) {
            logger.error("Failed to retrieve or parse data from Scryfall. {}", e.getMessage());
            throw new ServletException("Failed to retrieve or parse data from Scryfall", e);
        }

        JsonNode dataArrayNode = convertScryfallResponseToDataNode(scryfallResponse);
        List<Printing> returnedPrintings = convertDataNodeToPrintingList(dataArrayNode);
        List<Card> returnedCards = convertPrintingListToCardList(returnedPrintings, dataArrayNode);

        req.setAttribute("cards", returnedCards);
        req.setAttribute("searchLocation", "Scryfall");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/results.jsp");
        dispatcher.forward(req, resp);
    }

    public JsonNode convertScryfallResponseToDataNode(HttpResponse<String> scryfallResponse) {
        try {
            if (scryfallResponse.statusCode() == 200 && scryfallResponse.body() != null && !scryfallResponse.body().trim().isEmpty()) {
                JsonNode rootNode = mapper.readTree(scryfallResponse.body());
                JsonNode dataArrayNode =  rootNode.get("data");
                if (dataArrayNode != null &&  dataArrayNode.isArray()) {
                    return dataArrayNode;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse JSON tree structure: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Printing> convertDataNodeToPrintingList(JsonNode dataArrayNode) {
        if (dataArrayNode == null) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(dataArrayNode.traverse(), new TypeReference<>() {});
        } catch (Exception e) {
            logger.error("Failed mapping data node tokens to Printing POJO collection: {}", e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Card> convertPrintingListToCardList(List<Printing> printings, JsonNode dataArrayNode) {
        List<Card> cardList = new ArrayList<>();
        if (printings != null) {
            for (Printing printing : printings) {
                String cleanName = sanitizeCardName(printing);
                Integer cmcValue = sanitizeCmcValue(printings, printing, dataArrayNode);
                String cleanTypeLine = sanitizeTypeLine(printing);
                String imageUrl = retrieveImageUrl(printings, printing, dataArrayNode);

                Card card = new Card(cleanName, cmcValue, cleanTypeLine, 1);
                card.setImageUrl(imageUrl);

                cardList.add(card);
            }
        }
        return cardList;
    }

    public String sanitizeCardName(Printing printing) {
        String cleanName = printing.getName();
        if (cleanName == null || cleanName.trim().isEmpty()) {
            cleanName = "Unnamed Object / Token";
        }
        return cleanName;
    }

    public Integer sanitizeCmcValue(List<Printing> printings, Printing printing, JsonNode dataArrayNode) {
        Integer cmcValue = null;
        String rawCost = printing.getManaCost();
        if (rawCost == null && dataArrayNode != null && printings != null) {
            int index = printings.indexOf(printing);
            if (index >= 0 && index < dataArrayNode.size()) {
                JsonNode rawPrintingNode = dataArrayNode.get(index);
                if (rawPrintingNode != null && rawPrintingNode.has("card_faces")) {
                    rawCost = rawPrintingNode.get("card_faces").get(0).path("mana_cost").asText(null);
                }
            }
        }
        if (rawCost != null && !rawCost.isEmpty()) {
            if (printing.getCmc() instanceof Number) {
                cmcValue = ((Number) printing.getCmc()).intValue();
            }
        }
        return cmcValue;
    }

    public String sanitizeTypeLine(Printing printing) {
        String cleanTypeLine = printing.getTypeLine();
        if (cleanTypeLine != null) {
            cleanTypeLine = cleanTypeLine.replace("—", "-");
        } else {
            cleanTypeLine = "Token";
        }
        return cleanTypeLine;
    }

    public String retrieveImageUrl(List<Printing> printings, Printing printing, JsonNode dataArrayNode) {
        String imageUrl = null;
        if (printing.getImageUris() != null) {
            imageUrl = printing.getImageUris().getNormal();
        } else {
            try {
                JsonNode rawPrintingNode = dataArrayNode.get(printings.indexOf(printing));
                if (rawPrintingNode != null && rawPrintingNode.has("card_faces")) {
                    JsonNode cardFaces = rawPrintingNode.path("card_faces");
                    if (cardFaces.isArray() && !cardFaces.isEmpty()) {
                        imageUrl = cardFaces.get(0).path("image_uris").path("normal").asText(null);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        if (imageUrl == null) {
            imageUrl = "https://scryfall.io";
        }
        return imageUrl;
    }


}
