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

        // Someday, maybe I'll be able to make this webapp httpS capable.
        // On that day, I can use that url as the COGNITO_REDIRECT_URL.
        // This way, I won't need my own local tomcat server running in order to
        // use this webapp, I'll be able to just navigate to the webapp on any
        // device, at any time, and it'll work.  At least, that's my understanding
        // right now (11/1/26).  So, I'm keeping the 'if' check below, even though
        // there currently is no COGNITO_REDIRECT_URL property in my
        // ElasticBeanstalk environment.
        String redirectUrl = System.getenv("COGNITO_REDIRECT_URL");
        if (redirectUrl == null || redirectUrl.trim().isEmpty()) {
            redirectUrl = (String) context.getAttribute("cognito.redirectURL");
        }
        this.redirectUrl = redirectUrl;
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
