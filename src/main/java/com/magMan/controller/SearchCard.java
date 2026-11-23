package com.magMan.controller;

import com.magMan.entity.Card;
import com.magMan.persistence.GenericDao;

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

        GenericDao<Card> cardDao = new GenericDao<>(Card.class);

        if ("search".equals(req.getParameter("submit"))) {
            String searchName = req.getParameter("searchName");
            if ("true".equals(req.getParameter("exactName"))) {
                req.setAttribute("cards", cardDao.getByPropertyEqual("cardName", searchName));
            } else {
                req.setAttribute("cards", cardDao.getByPropertyLike("cardName", searchName));
            }
        } else if ("update".equals(req.getParameter("submit"))) {
            req.setAttribute("cards", cardDao.getByPropertyEqual("scryfallUuid", req.getParameter("searchTerm")));
        } else if ("viewAllCollection".equals(req.getParameter("submit"))) {
            req.setAttribute("cards", cardDao.getAll());
        }
        req.setAttribute("searchLocation", "Your Collection");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/results.jsp");
        dispatcher.forward(req, resp);
    }
}