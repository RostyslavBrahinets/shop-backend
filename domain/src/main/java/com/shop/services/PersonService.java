package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Person;
import com.shop.repositories.PersonRepository;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonValidator personValidator;
    private final ContactValidator contactValidator;

    public PersonService(
        PersonRepository personRepository,
        PersonValidator personValidator,
        ContactValidator contactValidator
    ) {
        this.personRepository = personRepository;
        this.personValidator = personValidator;
        this.contactValidator = contactValidator;
    }

    public List<Person> getPeople() {
        return personRepository.getPeople();
    }

    public Person addPerson(Person person) {
        personValidator.validate(person);
        personRepository.addPerson(person);
        return person;
    }

    public void deletePerson(long id) {
        personValidator.validate(id);
        personRepository.deletePerson(id);
    }

    public Person getPerson(long id) {
        personValidator.validate(id);
        Optional<Person> person = personRepository.getPerson(id);
        if (person.isEmpty()) {
            throw new NotFoundException("Person not found");
        } else {
            return person.get();
        }
    }

    public Person getPerson(String email) {
        contactValidator.validate(email);
        Optional<Person> person = personRepository.getPerson(email);
        if (person.isEmpty()) {
            throw new NotFoundException("Person not found");
        } else {
            return person.get();
        }
    }
}
