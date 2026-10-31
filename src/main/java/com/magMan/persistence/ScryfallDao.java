package com.magMan.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magMan.scryfall.JsonCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ScryfallDao {

    private final Logger logger = LogManager.getLogger(this.getClass());

    JsonCard getJsonCard() {
        Client client = ClientBuilder.newClient();
        // TODO read in the uri from a properties file
        WebTarget target =
                client.target("https://api.scryfall.com/cards/named")
                        .queryParam("exact", "shahrazad");
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

        ObjectMapper mapper = new ObjectMapper();
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
