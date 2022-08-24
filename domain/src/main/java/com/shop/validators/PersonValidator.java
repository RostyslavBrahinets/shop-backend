package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonValidator {
    public void validate(
        String firstName,
        String lastName
    ) {
        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("First name is invalid");
        } else if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Last name is invalid");
        }
    }

    public void validate(long id, List<Person> people) {
        List<Long> ids = new ArrayList<>();

        for (Person person : people) {
            ids.add(person.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Person not found");
        }
    }
}