package aar.repository;

import aar.models.Bolsa;
import aar.services.BolsaDaoInterface;

import java.util.List;

public class BolsaDaoImpl implements BolsaDaoInterface{
    @Override
    public void create(Bolsa bolsa) {
        JpaExecutor.executeInTransaction(em -> {
            em.persist(bolsa);
            return null;
        });
    }

    @Override
    public List<Bolsa> findAll() {
        return JpaExecutor.execute(em ->
            em.createQuery("SELECT u FROM Bolsa u", Bolsa.class).getResultList()
        );
    }  

    @Override
    public Bolsa findById(Long id) {
        return JpaExecutor.execute(em ->
            em.find(Bolsa.class, id)
        );
    }

    @Override
    public void update(Bolsa bolsa) {
        JpaExecutor.executeInTransaction(em -> {
            em.merge(bolsa);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        JpaExecutor.executeInTransaction(em -> {
            Bolsa bolsa = em.find(Bolsa.class, id);
            if (bolsa != null) {
                em.remove(bolsa);
            }
            return null;
        });
    }

    @Override
    public void init() {
        if (findAll().isEmpty()) {
            create(new Bolsa("Tecnologia"));
            create(new Bolsa("Economia"));
            create(new Bolsa("Reposteria"));
        }
    }
}
