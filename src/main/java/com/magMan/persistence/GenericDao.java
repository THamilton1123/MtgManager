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

import java.lang.reflect.Method;
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
     * update Entity
     * @param entity  Entity to be updated
     */
    public void update(T entity) {
        /*
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(entity);
        transaction.commit();
        session.close();
        */
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        }
    }

    /**
     * insert a new Entity
     * @param entity  Entity to be inserted
     */
    /*
    public int insert(T entity) {
        int id = 0;
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(entity);
        transaction.commit();
        id = getEntityId(entity);
        session.close();
        return id;
    }
    */
    public int insert(T entity) {
        int id;
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            id = getEntityId(entity);
            return id;
        }
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
            logger.debug("The list of {}: {} ", type, list);
            return list;
        }
    }

    /**
     * Get Entity by property (exact match)
     * sample usage: getByPropertyEqual("rulingDate", "2022-12-8")
     * sample usage: getByPropertyEqual("cardName", "Counterspell")
     */
    /*
    public List<T> getByPropertyEqual(String property, Object value) {
        Session session = getSession();

        logger.debug("Searching for {}s for " + propertyRulingDate + " = " + value, type);

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        query.select(root).where(builder.equal(root.get(propertyRulingDate), value));
        List<T> list = session.createSelectionQuery(query).getResultList();

        session.close();
        return list;
    }
    */
    public List<T> getByPropertyEqual(String property, Object value) {
        try (Session session = getSession()) {
            logger.debug("Searching for {}s by {} == {}", type, property, value);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            query.select(root).where(builder.equal(root.get(property), value));
            return session.createQuery(query).getResultList();
        }
    }

    /**
     * Get entity by property (like)
     * sample usage: getByPropertyLike("cardName", "in")
     * sample usage: getByPropertyLike("rulingText", "win the game")
     */
    /*
    public List<T> getByPropertyLike(String property, Object value) {
        Session session = getSession();

        logger.debug("Searching for  with {} = {}",  property, value);

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        Expression<String> propertyPath = root.get(property);

        query.where(builder.like(propertyPath, "%" + value + "%"));

        List<T> list = session.createQuery( query ).getResultList();
        session.close();
        return list;
    }
    */
    public List<T> getByPropertyLike(String property, Object value) {
        try (Session session = getSession()) {
            logger.debug("Searching for {}s by {} like {}", type, property, value);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            Expression<String> propertyPath = root.get(property);
            query.where(builder.like(propertyPath, "%" + value + "%"));
            return session.createQuery(query).getResultList();
        }
    }

    /**
     * Returns an open session from the SessionFactory
     * @return session
     */
    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }

    /**
     * Gets entity id.
     *
     * @param entity the entity
     * @return the entity id
     */
    public int getEntityId(T entity) {
        try {
            // Find the 'public int getId()' method on whatever entity is passed in
            Method method = entity.getClass().getMethod("getId");

            // Invoke returns an Object (Integer), but Java automatically
            // unboxes it down to a primitive 'int' to match this method's return type.
            return (Integer) method.invoke(entity);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error: " + entity.getClass().getSimpleName() + " is missing a getId() method!", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch ID via reflection", e);
        }
    }

}
