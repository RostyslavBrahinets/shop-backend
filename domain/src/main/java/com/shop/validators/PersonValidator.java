package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.*;
import com.shop.repositories.PersonRepository;

import java.util.ArrayList;
import java.util.List;

public class PersonValidator {
    private final PersonRepository personRepository;

    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void validate(Person person) {
        String firstName = person.getFirstName();
        String lastName = person.getLastName();
        Role role = person.getRole();
        Basket basket = person.getBasket();
        Contact contact = person.getContact();
        Wallet wallet = person.getWallet();

        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("First name is invalid");
        } else if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Last name is invalid");
        } else if (role == null || role.toString().isBlank()) {
            throw new ValidationException("Role is invalid");
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