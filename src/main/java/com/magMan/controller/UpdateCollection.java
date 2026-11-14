package com.magMan.controller;

import com.magMan.entity.Card;
import com.magMan.persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A simple servlet to add/remove cards to/from an MTG card collection.
 * @author thamilton12
 */

@WebServlet(
        urlPatterns = {"/UpdateCollection"}
)

public class UpdateCollection extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action =  req.getParameter("action");
        String cardName = req.getParameter("cardName");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        GenericDao<Card> cardDao = new GenericDao<>(Card.class);

        if ("add".equals(action)) {
            addCard(req, cardDao, cardName, quantity);
        } else if ("remove".equals(action)) {
            removeCard(req, cardDao, quantity);
        }

        String encodedCardName = URLEncoder.encode(cardName, StandardCharsets.UTF_8);
        resp.sendRedirect("searchCard?searchTerm=" + encodedCardName + "&submit=search");
    }

    /**
     * Logic for adding the user's selected card, by the user's entered quantity, to their collection
     * @param req      the http request
     * @param cardDao  the card data access object
     * @param cardName the name of the user's selected card
     * @param quantity the quantity of the user's selected card that the user has chosen to add
     */
    private void addCard(HttpServletRequest req, GenericDao<Card> cardDao, String cardName, int quantity) {
        String cardType = req.getParameter("cardType");
        String imageUrl = req.getParameter("imageUrl");
        int cardCmc = Integer.parseInt(req.getParameter("cardCmc"));

        List<Card> existingCards = cardDao.getByPropertyEqual("cardName", cardName);
        if (!existingCards.isEmpty()) {
            Card existingCard = existingCards.get(0);
            existingCard.setCardQuantity(existingCard.getCardQuantity() + quantity);
            cardDao.update(existingCard);
        } else {
            Card newCard = new Card(cardName, cardCmc, cardType, quantity);
            newCard.setImageUrl(imageUrl);
            cardDao.insert(newCard);
        }
    }

    /**
     * Logic for removing the user's selected card, by the user's entered quantity, from their collection
     * @param req      the http request
     * @param cardDao  the card data access object
     * @param quantity the quantity of the user's selected card that the user has chosen to remove
     */
    private void removeCard(HttpServletRequest req, GenericDao<Card> cardDao, int quantity) {
        int cardId = Integer.parseInt(req.getParameter("cardId"));
        Card existingCard = cardDao.getById(cardId);

        if (existingCard != null) {
            int remainingQuantity = existingCard.getCardQuantity() - quantity;
            if (remainingQuantity > 0) {
                existingCard.setCardQuantity(remainingQuantity);
                cardDao.update(existingCard);
            } else {
                cardDao.delete(existingCard);
            }
        }
    }
}
