package com.shop.services;

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

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(long id) {
        personValidator.validate(id, personRepository.findAll());
        Optional<Person> person = personRepository.findById(id);
        return person.orElseGet(Person::new);
    }

    public Person findByEmail(String email) {
        contactValidator.validate(email);
        Optional<Person> person = personRepository.findByEmail(email);
        return person.orElseGet(Person::new);
    }

    public Person save(
        String firstName,
        String lastName
    ) {
        personValidator.validate(firstName, lastName);
        return personRepository.save(firstName, lastName);
    }

    public Person update(
        long id,
        String firstName,
        String lastName
    ) {
        personValidator.validate(id, personRepository.findAll());
        personValidator.validate(firstName, lastName);
        return personRepository.update(id, firstName, lastName);
    }

    public void delete(long id) {
        personValidator.validate(id, personRepository.findAll());
        personRepository.delete(id);
    }
}
