package com.shop.repositories;

import com.shop.dao.UserRoleDao;
import com.shop.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRoleRepository {
    private final UserRoleDao userRoleDao;

    public UserRoleRepository(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public Optional<Role> findRoleForUser(long userId) {
        return userRoleDao.findRoleForUser(userId);
    }

    public void saveRoleForUser(long userId, long roleId) {
        userRoleDao.saveRoleForUser(userId, roleId);
    }

    public void updateRoleForUser(long userId, long roleId) {
        userRoleDao.updateRoleForUser(userId, roleId);
    }
}
