package com.moviedb.persistence;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory implements Serializable {

    private static EntityManagerFactory entityManagerFactory = null;
    private static EntityManager entityManager = null;

    public static EntityManager getEntityManager() {

        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("movieimdb");
        }

        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;

    }
}
