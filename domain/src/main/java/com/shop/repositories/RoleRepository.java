package com.shop.repositories;

import com.shop.dao.RoleDao;
import com.shop.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepository {
    private final RoleDao roleDao;

    public RoleRepository(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Optional<Role> getRole(long id) {
        return roleDao.getRole(id);
    }
}
