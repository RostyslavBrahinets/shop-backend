package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Person;
import com.shop.repositories.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonValidator {
    private final PersonRepository personRepository;

    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void validate(Person person) {
        String firstName = person.getFirstName();
        String lastName = person.getLastName();

        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("First name is invalid");
        } else if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Last name is invalid");
        }
    }

    public void validate(long id) {
        List<Long> ids = new ArrayList<>();

        for (Person person : personRepository.getPeople()) {
            ids.add(person.getId());
        }

        if (id < 1 || !ids.contains(id)) {
            throw new NotFoundException("Person not found");
        }
    }
}