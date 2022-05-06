package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Person;
import com.shop.models.Role;
import com.shop.repositories.PersonRoleRepository;
import com.shop.validators.PersonValidator;
import com.shop.validators.RoleValidator;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Person> getPeople(long roleId) {
        roleValidator.validate(roleId);
        return personRoleRepository.getPeople(roleId);
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
}
