package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Role;
import com.shop.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String name) {
        Optional<Role> role = roleRepository.getRoleByName(name);
        if (role.isEmpty()) {
            throw new NotFoundException("Role not found");
        } else {
            return role.get();
        }
    }
}
