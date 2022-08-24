package com.shop.validators;

import com.shop.exceptions.ValidationException;
import com.shop.models.Contact;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrationValidator {
    private final PersonValidator personValidator;
    private final ContactValidator contactValidator;

    public RegistrationValidator(
        PersonValidator personValidator,
        ContactValidator contactValidator
    ) {
        this.personValidator = personValidator;
        this.contactValidator = contactValidator;
    }

    public boolean isValidData(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        List<Contact> contacts
    ) throws ValidationException {
        personValidator.validate(firstName, lastName);
        contactValidator.validate(email, phone, password, contacts);

        return true;
    }
}
