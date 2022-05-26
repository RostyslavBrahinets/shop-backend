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

    public Optional<Role> findRoleForPerson(long personId) {
        return personRoleDao.findRoleForPerson(personId);
    }

    public void saveRoleForPerson(long personId, long roleId) {
        personRoleDao.saveRoleForPerson(personId, roleId);
    }

    public void updateRoleForPerson(long personId, long roleId) {
        personRoleDao.updateRoleForPerson(personId, roleId);
    }
}
