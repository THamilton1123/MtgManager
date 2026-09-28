package com.magMan.persistence;

import com.magMan.entity.Card;
import com.magMan.util.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDaoTest {

    CardDao cardDao;

    @BeforeEach
    void setUp() {
        cardDao = new CardDao();
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }

    @Test
    void getByIdSuccess() {
        Card retrievedCard = cardDao.getById(1);
        assertNotNull(retrievedCard);
        assertEquals("Counterspell", retrievedCard.getCardName());
    }

    @Test
    void updateSuccess() {
        Card cardToUpdate = cardDao.getById(1);
        cardToUpdate.setCardName("FakeCardName");
        cardDao.update(cardToUpdate);

        // retrieve the card and check that the name change worked
        Card actualCard = cardDao.getById(1);
        assertEquals("FakeCardName", actualCard.getCardName());
    }

    @Test
    void insertSuccess() {
        Card cardToInsert = new Card("Static Orb", 3, "Artifact", 1);
        int insertedCardId = cardDao.insert(cardToInsert);
        assertNotEquals(0, insertedCardId);
        Card insertedCard = cardDao.getById(insertedCardId);
        assertEquals("Static Orb", insertedCard.getCardName());
    }

    @Test
    void deleteSuccess() {
        cardDao.delete(cardDao.getById(2));
        assertNull(cardDao.getById(2));
    }

    @Test
    void getAllSuccess() {
        List<Card> cards = cardDao.getAll();
        assertEquals(8, cards.size());
    }

    @Test
    void getByPropertyEqualSuccess() {
        List<Card> cards = cardDao.getByPropertyEqual("cardName", "Jace, Wielder of Mysteries");
        assertEquals(1, cards.size());
        assertEquals(7, cards.get(0).getId());
    }

    @Test
    void getByPropertyLikeSuccess() {
        List<Card> cards = cardDao.getByPropertyLike("cardName", "in");
        assertEquals(3, cards.size());
    }

    @AfterAll
    static void cleanUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }
}