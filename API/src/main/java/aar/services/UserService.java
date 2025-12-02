package aar.services;

import aar.models.User;
import java.util.List;

public class UserService {

    private final UserDaoInterface userDao;

    public UserService(UserDaoInterface userDao) {
        this.userDao = userDao;
        userDao.init();
    }

    public void createUser(User user) {
        userDao.create(user);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}


