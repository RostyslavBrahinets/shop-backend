package com.shop.repositories;

import com.shop.dao.PersonRoleDao;
import com.shop.models.Person;
import com.shop.models.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonRoleRepository {
    private final PersonRoleDao personRoleDao;

    public PersonRoleRepository(PersonRoleDao personRoleDao) {
        this.personRoleDao = personRoleDao;
    }

    public List<Person> getPeople(long roleId) {
        return personRoleDao.getPeople(roleId);
    }

    public Optional<Role> getRole(long personId) {
        return personRoleDao.getRole(personId);
    }

    public void addRoleForPerson(long personId, long roleId) {
        personRoleDao.addRoleForPerson(personId, roleId);
    }
}
