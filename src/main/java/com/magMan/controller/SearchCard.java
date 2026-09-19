package com.magMan.controller;

import com.magMan.persistence.CardData;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * A simple servlet to browse a MTG card collection.
 * @author thamilton12
 */

@WebServlet(
        urlPatterns = {"/searchCard"}
)

public class SearchCard extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CardData cardData = new CardData();
        if (req.getParameter("submit").equals("search")) {
            req.setAttribute("cards", cardData.getCardsByName(req.getParameter("searchTerm")));
        } else {
            req.setAttribute("cards", cardData.getAllCards());
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/results.jsp");
        dispatcher.forward(req, resp);
    }
}