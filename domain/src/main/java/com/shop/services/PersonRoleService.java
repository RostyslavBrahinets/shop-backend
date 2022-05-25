package com.shop.services;

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

    public Role findRoleByPerson(long personId) {
        personValidator.validate(personId);
        Optional<Role> role = personRoleRepository.findRoleByPerson(personId);
        return role.orElseGet(Role::new);
    }

    public void saveRoleForPerson(long personId, long roleId) {
        personValidator.validate(personId);
        roleValidator.validate(roleId);
        personRoleRepository.saveRoleForPerson(personId, roleId);
    }

    public void updateRoleForPerson(long personId, String name) {
        personValidator.validate(personId);
        Role newRole = roleService.getRoleByName(name);
        personRoleRepository.updateRoleForPerson(personId, newRole.getId());
    }
}
