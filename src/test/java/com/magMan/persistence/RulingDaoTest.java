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
    CardDao cardDao;

    @BeforeEach
    void setUp() {
        rulingDao = new RulingDao();
        cardDao = new CardDao();
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }

    @Test
    void getByIdSuccess() {
        Ruling retrievedRuling = rulingDao.getById(1);
        Card urzaLordHighArtificer = cardDao.getById(3);
        assertNotNull(retrievedRuling);
        assertEquals("If a spell has X in its mana cost, you must choose 0 as the value of X when casting it without paying its mana cost.", retrievedRuling.getRulingText());
        assertEquals(LocalDate.parse("2022-12-08"), retrievedRuling.getRulingDate());
        assertEquals(urzaLordHighArtificer, retrievedRuling.getCard());
    }

    @Test
    void updateSuccess() {
        Ruling ruling = rulingDao.getById(2);
        ruling.setRulingDate(LocalDate.parse("9999-12-31"));
        rulingDao.update(ruling);

        Ruling updatedRuling = rulingDao.getById(2);
        assertEquals(LocalDate.parse("9999-12-31"), updatedRuling.getRulingDate());
    }

    @Test
    void insertSuccess() {
        // get a Card
        Card counterspell = cardDao.getById(1);

        // create a Ruling with that Card
        Ruling fakeRuling = new Ruling(LocalDate.parse("2999-12-31"), "This ruling is a fake!", counterspell);

        // insert the Ruling
        int insertedFakeRulingId = rulingDao.insert(fakeRuling);

        // retrieve the Ruling
        Ruling retrievedFakeRuling = rulingDao.getById(insertedFakeRulingId);

        // verify
        assertNotNull(retrievedFakeRuling);
        assertEquals(fakeRuling, retrievedFakeRuling);
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
    void getByPropertyEqualSuccess() {
        Ruling expectedRuling1 = rulingDao.getById(3);
        Ruling expectedRuling2 = rulingDao.getById(6);
        List<Ruling> rulings = rulingDao.getByPropertyEqual("rulingDate", LocalDate.parse("2018-07-13"));
        assertEquals(2, rulings.size());
        assertTrue(rulings.containsAll(List.of(expectedRuling1, expectedRuling2)));
    }

    @Test
    void getByPropertyLike() {
        Ruling expectedRuling1 = rulingDao.getById(5);
        Ruling expectedRuling2 = rulingDao.getById(6);
        List<Ruling> rulings = rulingDao.getByPropertyLike("rulingText", "abilities");
        assertEquals(2, rulings.size());
        assertTrue(rulings.containsAll(List.of(expectedRuling1, expectedRuling2)));
    }

    @AfterAll
    static void cleanUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }
}