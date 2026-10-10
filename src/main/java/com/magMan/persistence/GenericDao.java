package com.magMan.persistence;

import com.magMan.entity.Ruling;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.time.LocalDate;
import java.util.List;

public class GenericDao<T> {

    private Class<T> type;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Instantiates a new Generic dao.
     *
     * @param type the entity type, for example, Card
     */
    public GenericDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Gets an Entity by id.
     *
     * @param id the Entity id to search by
     * @return an Entity
     */
    /*
    public <T> T getById(int id) {
        Session session = getSession();
        T entity = (T) session.get(type, id);
        session.close();
        return entity;
    }
    */
    public T getById(int id) {
        try (Session session = getSession()) {
            return session.get(type, id);
        }
    }

    /**
     * update ruling
     * @param ruling  Ruling to be updated
     */
    public void update(Ruling ruling) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(ruling);
        transaction.commit();
        session.close();
    }

    /**
     * insert a new ruling
     * @param ruling  Ruling to be inserted
     */
    public int insert(Ruling ruling) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(ruling);
        transaction.commit();
        id = ruling.getId();
        session.close();
        return id;
    }

    /**
     * Delete an Entity
     * @param entity Entity to be deleted
     */
    /*
    public void delete(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
    }
    */
    public void delete(T entity) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        }
    }


    /**
     * Gets all Entities
     *
     * @return all Entities
     */
    /*
    public List<T> getAll() {
        Session session = getSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        List<T> list = session.createQuery(query).getResultList();
        logger.debug("The list of {} " + list, type);
        session.close();
        return list;
    }
    */
    public List<T> getAll() {
        try (Session session = getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            List<T> list = session.createQuery(query).getResultList();
            logger.debug("The list of {} " + list, type);
            return list;
        }
    }

    /**
     * Get ruling by property (exact match)
     * sample usage: getByPropertyEqual("rulingDate", "12/8/2022")
     */
    public List<Ruling> getByPropertyEqual(String propertyRulingDate, LocalDate value) {
        Session session = sessionFactory.openSession();

        logger.debug("Searching for rulings for " + propertyRulingDate + " = " + value);

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ruling> query = builder.createQuery(Ruling.class);
        Root<Ruling> root = query.from(Ruling.class);
        query.select(root).where(builder.equal(root.get(propertyRulingDate), value));
        List<Ruling> rulings = session.createSelectionQuery( query ).getResultList();

        session.close();
        return rulings;
    }

    /**
     * Get ruling by property (like)
     * sample usage: getByPropertyLike("rulingText", "win the game")
     */
    public List<Ruling> getByPropertyLike(String propertyRulingText, String value) {
        Session session = sessionFactory.openSession();

        logger.debug("Searching for ruling with {} = {}",  propertyRulingText, value);

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ruling> query = builder.createQuery(Ruling.class);
        Root<Ruling> root = query.from(Ruling.class);
        Expression<String> propertyPath = root.get(propertyRulingText);

        query.where(builder.like(propertyPath, "%" + value + "%"));

        List<Ruling> rulings = session.createQuery( query ).getResultList();
        session.close();
        return rulings;
    }

    /**
     * Returns an open session from the SessionFactory
     * @return session
     */
    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }

}
