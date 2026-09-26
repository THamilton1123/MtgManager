package com.magMan.persistence;

import com.magMan.entity.Card;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

public class CardDao {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * Get card by id
     */
    public Card getById(int id) {
        Session session = sessionFactory.openSession();
        Card card = session.get(Card.class, id);
        session.close();
        return card;
    }

    /**
     * update card
     * @param card  Card to be updated
     */
    public void update(Card card) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(card);
        transaction.commit();
        session.close();
    }

    /**
     * insert a new card
     * @param card  Card to be inserted
     */
    public int insert(Card card) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(card);
        transaction.commit();
        id = card.getCardId();
        session.close();
        return id;
    }

    /**
     * Delete a card
     * @param card Card to be deleted
     */
    public void delete(Card card) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(card);
        transaction.commit();
        session.close();
    }


    /** Return a list of all cards
     *
     * @return All cards
     */
    public List<Card> getAll() {

        Session session = sessionFactory.openSession();

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Card> query = builder.createQuery(Card.class);
        Root<Card> root = query.from(Card.class);
        List<Card> cards = session.createSelectionQuery( query ).getResultList();

        logger.debug("The list of cards " + cards);
        session.close();

        return cards;
    }

    /**
     * Get card by property (exact match)
     * sample usage: getByPropertyEqual("cardName", "Counterspell")
     */
    public List<Card> getByPropertyEqual(String propertyCardName, String value) {
        Session session = sessionFactory.openSession();

        logger.debug("Searching for card with " + propertyCardName + " = " + value);

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Card> query = builder.createQuery(Card.class);
        Root<Card> root = query.from(Card.class);
        query.select(root).where(builder.equal(root.get(propertyCardName), value));
        List<Card> cards = session.createSelectionQuery( query ).getResultList();

        session.close();
        return cards;
    }

    /**
     * Get card by property (like)
     * sample usage: getByPropertyLike("cardName", "in")
     */
    public List<Card> getByPropertyLike(String propertyCardName, String value) {
        Session session = sessionFactory.openSession();

        logger.debug("Searching for card with {} = {}",  propertyCardName, value);

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Card> query = builder.createQuery(Card.class);
        Root<Card> root = query.from(Card.class);
        Expression<String> propertyPath = root.get(propertyCardName);

        query.where(builder.like(propertyPath, "%" + value + "%"));

        List<Card> cards = session.createQuery( query ).getResultList();
        session.close();
        return cards;
    }

}
