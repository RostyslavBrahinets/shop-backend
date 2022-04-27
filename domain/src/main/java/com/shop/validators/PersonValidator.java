package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.*;
import com.shop.repositories.PersonRepository;

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
        } else if (basket == null) {
            throw new ValidationException("Basket is invalid");
        } else if (contact == null) {
            throw new ValidationException("Contact is invalid");
        } else if (wallet == null) {
            throw new ValidationException("Wallet is invalid");
        }
    }

    public void validate(int id) {
        if (id < 1 || id > personRepository.getPeople().size()) {
            throw new NotFoundException("Person not found");
        }
    }
}