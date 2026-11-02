package com.magMan.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.magMan.entity.Card;
import com.magMan.persistence.GenericDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/mycards/{cardname}")
public class MtgManApi {
    private final Logger logger = LogManager.getLogger(this.getClass());
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media types "text/plain", "application/json", or "text/html"
    @Produces({"text/plain", "application/json", "text/html"})
    public Response getOneOfMyCards(@PathParam("cardname") String cardName, @Context HttpHeaders headers) {
        GenericDao<Card> cardDao = new GenericDao<>(Card.class);
        List<Card> allMyCards = cardDao.getAll();
        List<Card> retrievedCards = new ArrayList<>();
        for  (Card card : allMyCards) {
            if(card.getCardName().equals(cardName)) {
                retrievedCards.add(card);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonOutput = null;
        try {
            jsonOutput = mapper.writeValueAsString(retrievedCards);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while creating json output.  Cards being converted: {}", retrievedCards);
        }

        List<MediaType> acceptableMediaTypes = headers.getAcceptableMediaTypes();
        for (MediaType type : acceptableMediaTypes) {
            if (type.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                return Response.status(200).type(MediaType.APPLICATION_JSON).entity(jsonOutput).build();
            }
            if (type.isCompatible(MediaType.TEXT_HTML_TYPE)) {
                output = "<h3>" + baseMessage + "</h3>";
                return Response.status(200).type(MediaType.TEXT_HTML).entity(output).build();
            }
        }

        return Response.status(200).type(MediaType.TEXT_PLAIN).entity(output).build();
    }
}
