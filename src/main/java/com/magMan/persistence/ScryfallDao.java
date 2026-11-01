package com.magMan.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magMan.scryfall.JsonCard;
import com.magMan.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

public class ScryfallDao implements PropertiesLoader {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final ObjectMapper mapper = new ObjectMapper();
    private final Properties scryProps = loadProperties("/scryfall.properties");
    private final String SCRY_URI = scryProps.getProperty("uri");

    JsonCard getJsonCardByNameLike(String cardName) {
        Client client = ClientBuilder.newClient();
        WebTarget target =
                client.target(SCRY_URI + "/named")
                        .queryParam("fuzzy", cardName);
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

        JsonCard card = null;
        try {
            card = mapper.readValue(response, JsonCard.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("ScryfallDao getJsonCard() JsonProcessingException", e);
        }

        return card;
    }
}
