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

    public List<Person> getPeople() {
        return personDao.getPeople();
    }

    public void addPerson(Person person) {
        personDao.addPerson(person);
    }

    public void deletePerson(long id) {
        personDao.deletePerson(id);
    }

    public Optional<Person> getPerson(long id) {
        return personDao.getPerson(id);
    }

    public Optional<Person> getPerson(String email) {
        return personDao.getPerson(email);
    }

    public void updatePerson(long id, Person updatedPerson) {
        personDao.updatePerson(id, updatedPerson);
    }
}
