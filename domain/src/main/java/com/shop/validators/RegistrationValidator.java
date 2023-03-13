package com.shop.validators;

import com.shop.exceptions.ValidationException;
import com.shop.models.AdminNumber;
import com.shop.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrationValidator {
    private final UserValidator userValidator;
    private final AdminNumberValidator adminNumberValidator;

    public RegistrationValidator(
        UserValidator userValidator,
        AdminNumberValidator adminNumberValidator
    ) {
        this.userValidator = userValidator;
        this.adminNumberValidator = adminNumberValidator;
    }

    public boolean isValidData(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        String adminNumber,
        List<User> users,
        List<AdminNumber> adminNumbers
    ) throws ValidationException {
        userValidator.validate(
            firstName,
            lastName,
            email,
            phone,
            password,
            users
        );

        adminNumberValidator.validate(
            adminNumber,
            adminNumbers
        );

        return true;
    }
}
