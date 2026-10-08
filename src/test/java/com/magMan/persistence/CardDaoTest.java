package com.magMan.persistence;

import com.magMan.entity.Card;
import com.magMan.entity.Ruling;
import com.magMan.util.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDaoTest {

    CardDao cardDao;
    RulingDao rulingDao;

    @BeforeEach
    void setUp() {
        cardDao = new CardDao();
        rulingDao = new RulingDao();
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
        assertEquals(cardToInsert, insertedCard);
    }

    @Test
    void deleteWithRulingsSuccess() {
        // get the card we want to delete that has multiple rulings associated
        Card urzaLordHighArtificer = cardDao.getById(3);
        List<Ruling> rulings = urzaLordHighArtificer.getRulings();

        // get the associated rulings' ID numbers
        // since this card has more rulings than only the two I have in the database, I used a loop
        // so whether I associate more rulings to this cord in the future or not, this test should work
        ArrayList<Number> rulingsIdNumbers = new ArrayList<>();
        for (Ruling ruling : rulings) {
            int rulingId = ruling.getId();
            rulingsIdNumbers.add(rulingId);
        }

        // delete the card
        cardDao.delete(urzaLordHighArtificer);

        // verify the card was deleted
        assertNull(cardDao.getById(3));

        // verify the associated rulings were also deleted
        for (Number currentRulingId : rulingsIdNumbers) {
            assertNull(rulingDao.getById((int) currentRulingId));
        }
    }

    @Test
    void getAllSuccess() {
        List<Card> cards = cardDao.getAll();
        assertEquals(8, cards.size());
    }

    @Test
    void getByPropertyEqualSuccess() {
        Card jaceWielderOfMysteries = cardDao.getById(7);
        List<Card> cards = cardDao.getByPropertyEqual("cardName", "Jace, Wielder of Mysteries");
        assertEquals(1, cards.size());
        assertEquals(jaceWielderOfMysteries, cards.get(0));
    }

    @Test
    void getByPropertyLikeSuccess() {
        Card solRing = cardDao.getById(2);
        Card tinker = cardDao.getById(5);
        Card weldingJar = cardDao.getById(8);
        List<Card> cards = cardDao.getByPropertyLike("cardName", "in");
        assertEquals(3, cards.size());
        assertTrue(cards.containsAll(List.of(solRing, tinker, weldingJar)));
    }

    @AfterAll
    static void cleanUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
    }
}