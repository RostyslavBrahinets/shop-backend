package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Role;
import com.shop.repositories.PersonRoleRepository;
import com.shop.validators.PersonValidator;
import com.shop.validators.RoleValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonRoleService {
    private final PersonRoleRepository personRoleRepository;
    private final PersonValidator personValidator;
    private final RoleService roleService;
    private final RoleValidator roleValidator;

    public PersonRoleService(
        PersonRoleRepository personRoleRepository,
        PersonValidator personValidator,
        RoleService roleService,
        RoleValidator roleValidator
    ) {
        this.personRoleRepository = personRoleRepository;
        this.personValidator = personValidator;
        this.roleService = roleService;
        this.roleValidator = roleValidator;
    }

    public Role getRole(long personId) {
        personValidator.validate(personId);
        Optional<Role> role = personRoleRepository.getRole(personId);
        if (role.isEmpty()) {
            throw new NotFoundException("Role for this person not found");
        } else {
            return role.get();
        }
    }

    public void addRoleForPerson(long personId, long roleId) {
        personValidator.validate(personId);
        roleValidator.validate(roleId);
        personRoleRepository.addRoleForPerson(personId, roleId);
    }

    public void updateRoleForPerson(long personId, String role) {
        personValidator.validate(personId);
        Role newRole = roleService.getRoleByName(role);
        personRoleRepository.updateRoleForPerson(personId, newRole.getId());
    }
}
