package com.shop.validators;

import com.shop.exceptions.ValidationException;
import org.springframework.stereotype.Component;

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
        String password
    ) throws ValidationException {
        personValidator.validate(firstName, lastName);
        contactValidator.validate(email, phone, password);

        return true;
    }
}
