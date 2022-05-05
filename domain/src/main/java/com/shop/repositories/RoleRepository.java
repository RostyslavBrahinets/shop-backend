package com.shop.repositories;

import com.shop.dao.RoleDao;
import com.shop.models.Role;

import java.util.Optional;

public class RoleRepository {
    private final RoleDao roleDao;

    public RoleRepository(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Optional<Role> getRole(long id) {
        return roleDao.getRole(id);
    }
}
