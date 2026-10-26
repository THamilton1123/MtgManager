package com.magMan.controller;

import com.magMan.util.PropertiesLoader;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Properties;

/**
 * Initializes the Cognito Exercise Application
 *
 * @author thamilton12
 */
@WebServlet(
        name = "cognitoStartup",
        urlPatterns = { "/cognito-startup" },
        loadOnStartup = 1
)
public class ApplicationStartup extends HttpServlet implements PropertiesLoader {

    private Properties properties;

    /**
     * No-arg constructor
     */
    public ApplicationStartup() {
    }

    /**
     * Init method
     */
    public void init() {
        try {
            properties = loadProperties("/cognito.properties");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ServletContext context = getServletContext();

        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            context.setAttribute("cognito." + key, value);
        }
    }
}