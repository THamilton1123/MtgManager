package com.magMan.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        urlPatterns = {"/logIn"}
)

/** Begins the authentication process using AWS Cognito
 *
 */
public class LogIn extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private String clientId;
    private String loginUrl;
    private String redirectUrl;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.clientId = (String) context.getAttribute("cognito.client.id");
        this.loginUrl = (String) context.getAttribute("cognito.loginURL");
        this.redirectUrl = (String) context.getAttribute("cognito.redirectURL");
    }

    /**
     * Route to the aws-hosted cognito login page.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TO DO if properties weren't loaded properly, route to an error page
        if (loginUrl == null || clientId == null || redirectUrl == null) {
            logger.error("Cognito configuration properties missing from ServletContext");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication system misconfigured");
            return;
        }

        String url = loginUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl;
        logger.info("Redirecting user to Cognito login: {}", url);
        resp.sendRedirect(url);
    }
}
