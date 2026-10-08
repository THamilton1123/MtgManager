package com.magMan.persistence;

import com.magMan.entity.Card;
import com.magMan.entity.Ruling;
import com.magMan.util.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RulingDaoTest {

    RulingDao rulingDao;

    @BeforeEach
    void setUp() {
        rulingDao = new RulingDao();
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }

    @Test
    void getByIdSuccess() {
        Ruling retrievedRuling = rulingDao.getById(1);
        assertNotNull(retrievedRuling);
        assertEquals("If a spell has X in its mana cost, you must choose 0 as the value of X when casting it without paying its mana cost.", retrievedRuling.getRulingText());
        assertEquals(3, retrievedRuling.getCard().getId());
    }

    @Test
    void update() {
    }

    @Test
    void insert() {
        // get a Card
        CardDao cardDao = new CardDao();
        Card retrievedCard = cardDao.getById(1);

        // create a Ruling with that Card
        Ruling fakeRuling = new Ruling(LocalDate.parse("2999-12-31"), "This ruling is a fake!", retrievedCard);

        // insert the Ruling
        int insertedFakeRulingId = rulingDao.insert(fakeRuling);

        // retrieve the Ruling
        Ruling retrievedFakeRuling = rulingDao.getById(insertedFakeRulingId);

        // verify
        assertNotNull(retrievedFakeRuling);
        assertEquals(fakeRuling.getRulingText(), retrievedFakeRuling.getRulingText());
        assertEquals(fakeRuling.getCard(), retrievedFakeRuling.getCard());
    }

    @Test
    void deleteSuccess() {
        rulingDao.delete(rulingDao.getById(6));
        assertNull(rulingDao.getById(6));
    }

    @Test
    void getAllSuccess() {
        List<Ruling> rulings = rulingDao.getAll();
        assertEquals(6, rulings.size());
    }

    @Test
    void getByPropertyEqual() {
    }

    @Test
    void getByPropertyLike() {
    }

    @AfterAll
    static void cleanUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }
}