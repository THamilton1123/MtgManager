package com.magMan.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * This file provides a SessionFactory for use with DAOs using Hibernate
 *
 * @author paulawaite
 * @version 3.0
 */
public class SessionFactoryProvider {

    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    /**
     * Create session factory.
     */
    public static void createSessionFactory() {

        // Load defaults from hibernate.cfg.xml (local database.properties)
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder().configure();

        // If running on AWS Elastic Beanstalk, override with RDS environment properties
        if (System.getenv("RDS_HOSTNAME") != null) {
            String dbUrl = "jdbc:mysql://" + System.getenv("RDS_HOSTNAME") + ":"
                    + System.getenv("RDS_PORT") + "/" + System.getenv("RDS_DB_NAME");

            registryBuilder.applySetting("hibernate.connection.url", dbUrl);
            registryBuilder.applySetting("hibernate.connection.username", System.getenv("RDS_USERNAME"));
            registryBuilder.applySetting("hibernate.connection.password", System.getenv("RDS_PASSWORD"));
        }

        // Create registry
        registry = registryBuilder.build();

        // Create MetadataSources
        MetadataSources sources = new MetadataSources(registry);

        // Create Metadata
        Metadata metadata = sources.getMetadataBuilder().build();

        // Create SessionFactory
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }
        return sessionFactory;

    }
}