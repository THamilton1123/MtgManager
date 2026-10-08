package com.magMan.persistence;

import com.magMan.entity.Ruling;
import com.magMan.util.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
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