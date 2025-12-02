package aar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Function;

public class JpaExecutor {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("InMemH2DB");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static <T> T execute(Function<EntityManager, T> action) {
        EntityManager em = getEntityManager();
        try {
            return action.apply(em);
        } finally {
            em.close();
        }
    }

    public static <T> T executeInTransaction(Function<EntityManager, T> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public static void closeFactory() {
        emf.close();
    }
}
