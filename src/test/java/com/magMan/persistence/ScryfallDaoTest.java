package com.magMan.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScryfallDaoTest {

    @Test
    void getPrintingByNameLike() {
        ScryfallDao dao = new ScryfallDao();
        assertEquals("0014def3-4063-4929-ac51-76aef1bb2a68",
                dao.getPrintingByNameLike("Shahrazad").getId());
    }
}