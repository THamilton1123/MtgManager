package com.magMan.controller;

import com.magMan.persistence.CardDao;

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

        CardDao cardDao = new CardDao();

        if (req.getParameter("submit").equals("search")) {
            req.setAttribute("cards", cardDao.getByPropertyLike("cardName", req.getParameter("searchTerm")));
        } else {
            req.setAttribute("cards", cardDao.getAll());
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/results.jsp");
        dispatcher.forward(req, resp);
    }
}