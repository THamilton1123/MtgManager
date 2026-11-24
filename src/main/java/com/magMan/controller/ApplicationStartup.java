package com.magMan.controller;

import com.magMan.persistence.ScryfallDao;
import com.magMan.util.PropertiesLoader;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.List;
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
        // Get cognito.properties
        try {
            properties = loadProperties("/cognito.properties");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Get Servlet Context
        ServletContext context = getServletContext();

        // Add each Cognito property to context as attribute
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            context.setAttribute("cognito." + key, value);
        }

        // Add all MTG sets as context attribute for dropdown search field in index.jsp
        ScryfallDao scryfallDao = new ScryfallDao();
        context.setAttribute("scryfallSets", scryfallDao.getAllSets());

        // Add all six rarities as a context attribute for dropdown search field in index.jsp
        context.setAttribute("rarities",
                List.of("Common", "Uncommon", "Rare", "Mythic", "Special", "Bonus"));

        // Add all six Super Types as a context attribute for dropdown search field in index.jsp
        context.setAttribute("superTypes",
                List.of("Basic", "Legendary", "Snow", "World", "Ongoing", "Host"));
    }
}