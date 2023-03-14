package com.shop.sign_up;

import com.shop.admin_number.AdminNumberValidator;
import com.shop.exceptions.ValidationException;
import com.shop.admin_number.AdminNumber;
import com.shop.user.User;
import com.shop.user.UserValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignUpValidator {
    private final UserValidator userValidator;
    private final AdminNumberValidator adminNumberValidator;

    public SignUpValidator(
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
