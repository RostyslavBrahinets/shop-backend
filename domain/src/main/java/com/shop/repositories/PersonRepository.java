package com.shop.repositories;

import com.shop.dao.PersonDao;
import com.shop.models.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepository {
    private final PersonDao personDao;

    public PersonRepository(PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<Person> findAll() {
        return personDao.findAll();
    }

    public Optional<Person> findById(long id) {
        return personDao.findById(id);
    }

    public Optional<Person> findByEmail(String email) {
        return personDao.findByEmail(email);
    }

    public Person save(
        String firstName,
        String lastName
    ) {
        personDao.save(
            firstName,
            lastName
        );

        return Person.of(firstName, lastName);
    }

    public Person update(
        long id,
        String firstName,
        String lastName
    ) {
        personDao.update(
            id,
            firstName,
            lastName
        );

        return Person.of(firstName, lastName).withId(id);
    }

    public void delete(long id) {
        personDao.delete(id);
    }

    public int count() {
        return personDao.findAll().size();
    }
}
