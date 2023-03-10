package com.shop.repositories;

import com.shop.dao.UserDao;
import com.shop.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User save(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        long adminNumberId
    ) {
        userDao.save(
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumberId
        );

        return User.of(
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumberId
        );
    }

    public User update(
        long id,
        String firstName,
        String lastName
    ) {
        userDao.update(
            id,
            firstName,
            lastName
        );

        return User.of(firstName, lastName).withId(id);
    }

    public void delete(long id) {
        userDao.delete(id);
    }

    public int count() {
        return userDao.findAll().size();
    }
}
