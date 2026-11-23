package com.magMan.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.magMan.entity.Card;
import com.magMan.persistence.ScryfallDao;
import com.magMan.scryfall.Printing;
import com.magMan.scryfall.ScryfallSearchCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple servlet to browse an MTG card collection.
 * @author thamilton12
 */

@WebServlet(
        urlPatterns = {"/searchScryfall"}
)

public class SearchScryfall extends HttpServlet {
    private final Logger logger;
    private final ObjectMapper mapper;
    private final ScryfallDao scryfallDao;

    public SearchScryfall() {
        this.logger = LogManager.getLogger(this.getClass());
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.scryfallDao = new ScryfallDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String submitAction = req.getParameter("submit");
        JsonNode dataArrayNode = null;

        if ("search".equals(submitAction)) {
            ScryfallSearchCriteria criteria = buildCriteriaFromRequest(req);
            if (scryfallDao.buildQueryString(criteria).isEmpty()) {
                resp.sendRedirect("index.jsp");
                return;
            }
            dataArrayNode = scryfallDao.searchScryfall(criteria);

        } else if ("searchByUuid".equals(submitAction)) {
            String uuid = req.getParameter("scryfallUuid");
            if (uuid == null ||  uuid.trim().isEmpty()) {
                resp.sendRedirect("index.jsp");
                return;
            }
            dataArrayNode = scryfallDao.getPrintingByUuid(uuid);
        }

        List<Printing> returnedPrintings = convertDataNodeToPrintingList(dataArrayNode);
        List<Card> returnedCards = convertPrintingListToCardList(returnedPrintings, dataArrayNode);

        req.setAttribute("cards", returnedCards);
        req.setAttribute("searchLocation", "Scryfall");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/results.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     *
     * @param req
     * @return
     */
    private ScryfallSearchCriteria buildCriteriaFromRequest(HttpServletRequest req) {
        ScryfallSearchCriteria criteria = new ScryfallSearchCriteria();

        String searchName = req.getParameter("searchName");
        if (searchName != null && !searchName.trim().isEmpty()) {
            criteria.setName(searchName);
        }

        boolean isExact = "true".equals(req.getParameter("exactName"));
        criteria.setExactName(isExact);

        // Show all printings when looking up an exact card name (or when explicitly requested)
        if (isExact || "true".equals(req.getParameter("allPrintings"))) {
            criteria.setAllPrintings(true);
        }

        if ("true".equals(req.getParameter("includeExtras"))) {
            criteria.setIncludeExtras(true);
        }

        return criteria;
    }

    /**
     *
     * @param dataArrayNode
     * @return
     */
    private List<Printing> convertDataNodeToPrintingList(JsonNode dataArrayNode) {
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

    /**
     *
     * @param printings
     * @param dataArrayNode
     * @return
     */
    private List<Card> convertPrintingListToCardList(List<Printing> printings, JsonNode dataArrayNode) {
        List<Card> cardList = new ArrayList<>();
        if (printings != null) {
            for (Printing printing : printings) {
                String cleanName = sanitizeCardName(printing);
                Integer cmcValue = sanitizeCmcValue(printings, printing, dataArrayNode);
                String cleanTypeLine = sanitizeTypeLine(printing);
                String imageUrl = retrieveImageUrl(printings, printing, dataArrayNode);
                String scryfallUuid = printing.getId();

                Card card = new Card(cleanName, cmcValue, cleanTypeLine, 1);
                card.setImageUrl(imageUrl);
                card.setScryfallUuid(scryfallUuid);

                cardList.add(card);
            }
        }
        return cardList;
    }

    /**
     *
     * @param printing
     * @return
     */
    private String sanitizeCardName(Printing printing) {
        String cleanName = printing.getName();
        if (cleanName == null || cleanName.trim().isEmpty()) {
            cleanName = "Unnamed Object / Token";
        }
        return cleanName;
    }

    /**
     *
     * @param printings
     * @param printing
     * @param dataArrayNode
     * @return
     */
    private Integer sanitizeCmcValue(List<Printing> printings, Printing printing, JsonNode dataArrayNode) {
        Integer cmcValue = null;
        String rawCost = printing.getManaCost();
        if (rawCost == null && dataArrayNode != null && printings != null) {
            int index = printings.indexOf(printing);
            if (index >= 0 && index < dataArrayNode.size()) {
                JsonNode rawPrintingNode = dataArrayNode.get(index);
                if (rawPrintingNode != null && rawPrintingNode.has("card_faces")) {
                    JsonNode cardFaces = rawPrintingNode.path("card_faces");
                    if (cardFaces.isArray() && !cardFaces.isEmpty()) {
                        rawCost = cardFaces.get(0).path("mana_cost").asText(null);
                    }
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

    /**
     *
     * @param printing
     * @return
     */
    private String sanitizeTypeLine(Printing printing) {
        String cleanTypeLine = printing.getTypeLine();
        if (cleanTypeLine != null) {
            cleanTypeLine = cleanTypeLine.replace("—", "-");
        } else {
            cleanTypeLine = "Token";
        }
        return cleanTypeLine;
    }

    /**
     *
     * @param printings
     * @param printing
     * @param dataArrayNode
     * @return
     */
    private String retrieveImageUrl(List<Printing> printings, Printing printing, JsonNode dataArrayNode) {
        String imageUrl = null;
        if (printing.getImageUris() != null) {
            imageUrl = printing.getImageUris().getNormal();
        } else if (dataArrayNode != null && printings != null) {
            try {
                int index = printings.indexOf(printing);
                if (index >= 0 && index < dataArrayNode.size()) {
                    JsonNode rawPrintingNode = dataArrayNode.get(index);
                    if (rawPrintingNode != null && rawPrintingNode.has("card_faces")) {
                        JsonNode cardFaces = rawPrintingNode.path("card_faces");
                        if (cardFaces.isArray() && !cardFaces.isEmpty()) {
                            imageUrl = cardFaces.get(0).path("image_uris").path("normal").asText(null);
                        }
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
