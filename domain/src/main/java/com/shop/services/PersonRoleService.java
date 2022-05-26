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
    private final RoleValidator roleValidator;

    public PersonRoleService(
        PersonRoleRepository personRoleRepository,
        PersonValidator personValidator,
        RoleValidator roleValidator
    ) {
        this.personRoleRepository = personRoleRepository;
        this.personValidator = personValidator;
        this.roleValidator = roleValidator;
    }

    public Role findRoleForPerson(long personId) {
        personValidator.validate(personId);
        Optional<Role> role = personRoleRepository.findRoleForPerson(personId);
        return role.orElseGet(Role::new);
    }

    public void saveRoleForPerson(long personId, long roleId) {
        personValidator.validate(personId);
        roleValidator.validate(roleId);
        personRoleRepository.saveRoleForPerson(personId, roleId);
    }

    public void updateRoleForPerson(long personId, long roleId) {
        personValidator.validate(personId);
        roleValidator.validate(roleId);
        personRoleRepository.updateRoleForPerson(personId, roleId);
    }
}
