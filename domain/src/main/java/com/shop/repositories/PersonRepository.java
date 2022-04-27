package com.shop.repositories;

import com.shop.dao.PersonDao;
import com.shop.models.Person;

import java.util.List;
import java.util.Optional;

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

    public void updatePerson(int id, Person person) {
        personDao.updatePerson(id, person);
    }

    public void deletePerson(int id) {
        personDao.deletePerson(id);
    }

    public Optional<Person> getPerson(int id) {
        return personDao.getPerson(id);
    }
}
