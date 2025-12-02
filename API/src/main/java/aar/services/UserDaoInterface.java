package aar.services;

import aar.models.User;
import java.util.List;

public interface UserDaoInterface {
    void create(User user);
    List<User> findAll();
    User findById(Long id);
    void update(User user);
    void delete(Long id);
    void init();
}
