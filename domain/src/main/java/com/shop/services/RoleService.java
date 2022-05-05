package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Role;
import com.shop.repositories.RoleRepository;

import java.util.Optional;

public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(
        RoleRepository roleRepository
    ) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(long id) {
        Optional<Role> role = roleRepository.getRole(id);
        if (role.isEmpty()) {
            throw new NotFoundException("Role for this person not found");
        } else {
            return role.get();
        }
    }
}
