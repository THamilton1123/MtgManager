package com.magMan.persistence;

import com.magMan.entity.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Access cards in the cards table.
 *
 * @author thamilton12
 */
public class CardData {

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Gets all cards.
     *
     * @return all cards
     */
    public List<Card> getAllCards() {
        String sql = "SELECT * FROM cards";
        logger.info("\nHere's the selectAllCards sql: " + sql);
        return executeQuery(sql);
    }

    /**
     * Gets cards by name.
     *
     * @param cardName the card's name
     * @return the cards by name
     */
    public List<Card> getCardsByName(String cardName) {
        String sql = "SELECT * FROM cards WHERE cardName like '%" + cardName + "%'";
        logger.info("\nHere's the selectCardsByName sql: " + sql);
        return executeQuery(sql);
    }

    /**
     * Connects to database and executes query passed as parameter
     * @param sql
     * @return List of cards returned by sql query
     */
    private List<Card> executeQuery(String sql) {
        List<Card> cards = new ArrayList<Card>();
        Database database = Database.getInstance();
        Connection connection = null;

        try {
            database.connect();
            connection = database.getConnection();
            Statement selectStatement = connection.createStatement();
            ResultSet results = selectStatement.executeQuery(sql);
            while (results.next()) {
                Card card = createCardFromResults(results);
                cards.add(card);
            }
            database.disconnect();
        } catch (SQLException e) {
            logger.error("SearchCard.getAllCards()...SQL Exception: ", e);
        } catch (Exception e) {
            logger.error("SearchCard.getAllCards()...Exception: ", e);
        }
        return cards;
    }

    /**
     * Create and return a Card object from the resultset
     * @param results
     * @return Card object with fields mapped
     * @throws SQLException
     */
    private Card createCardFromResults(ResultSet results) throws SQLException {
        Card card = new Card();
        card.setCardId(results.getInt("id"));
        card.setCardName(results.getString("cardName"));
        card.setCardCmc(results.getInt("cardCmc"));
        card.setCardType(results.getString("cardType"));
        return card;
    }

}