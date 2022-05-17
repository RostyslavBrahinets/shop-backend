package com.shop.repositories;

import com.shop.dao.PersonRoleDao;
import com.shop.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersonRoleRepository {
    private final PersonRoleDao personRoleDao;

    public PersonRoleRepository(PersonRoleDao personRoleDao) {
        this.personRoleDao = personRoleDao;
    }

    public Optional<Role> getRole(long personId) {
        return personRoleDao.getRole(personId);
    }

    public void addRoleForPerson(long personId, long roleId) {
        personRoleDao.addRoleForPerson(personId, roleId);
    }

    public void updateRoleForPerson(long personId, long roleId) {
        personRoleDao.updateRoleForPerson(personId, roleId);
    }
}
