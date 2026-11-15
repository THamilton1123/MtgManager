package com.magMan.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(
        urlPatterns = {"/logOut"}
)

public class LogOut extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        ServletContext context = getServletContext();
        String clientId = (String) context.getAttribute("cognito.client.id");
        String logoutUrl = (String) context.getAttribute("cognito.logoutURL");

        String logoutRedirectUrl = System.getenv("COGNITO_LOGOUT_REDIRECT_URL");
        if (logoutRedirectUrl == null || logoutRedirectUrl.isEmpty()) {
            logoutRedirectUrl = (String) context.getAttribute("cognito.logoutRedirectURL");
        }

        // 3. Build the full Cognito logout redirect
        String fullLogoutUrl = logoutUrl + "?client_id=" + clientId
                + "&logout_uri=" + URLEncoder.encode(logoutRedirectUrl, StandardCharsets.UTF_8);

        resp.sendRedirect(fullLogoutUrl);
    }
}
