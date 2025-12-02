package aar.repository;

import aar.models.User;
import aar.services.UserDaoInterface;

import java.util.List;

public class UserDaoImpl implements UserDaoInterface {

    @Override
    public void create(User user) {
        JpaExecutor.executeInTransaction(em -> {
            em.persist(user);
            return null;
        });
    }

    @Override
    public List<User> findAll() {
        return JpaExecutor.execute(em ->
            em.createQuery("SELECT u FROM User u", User.class).getResultList()
        );
    }

    @Override
    public User findById(Long id) {
        return JpaExecutor.execute(em ->
            em.find(User.class, id)
        );
    }

    @Override
    public void update(User user) {
        JpaExecutor.executeInTransaction(em -> {
            em.merge(user);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        JpaExecutor.executeInTransaction(em -> {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            return null;
        });
    }

    @Override
    public void init() {
        if (findAll().isEmpty()) {
            create(new User("Louise Pearce", "Journalist"));
            create(new User("Anton Grace", "Doctor"));
            create(new User("Maria Obama", "Teacher"));
        }
    }
}