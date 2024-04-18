package com.shop.signup;

import com.shop.adminnumber.AdminNumberValidator;
import com.shop.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.shop.adminnumber.AdminNumberParameter.getAdminNumberWithId;
import static com.shop.adminnumber.AdminNumberParameter.getNumber;
import static com.shop.user.UserParameter.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignUpValidatorTest {
    private SignUpValidator signUpValidator;
    private UserValidator userValidator;
    private AdminNumberValidator adminNumberValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userValidator = new UserValidator();
        adminNumberValidator = new AdminNumberValidator();

        signUpValidator = new SignUpValidator(
            userValidator,
            adminNumberValidator
        );
    }

    @Test
    @DisplayName("User data validated without exceptions")
    void user_data_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> userValidator.validate(
                getFirstName(),
                getLastName(),
                getEmail(),
                getPhone(),
                getPassword(),
                getUsers()
            )
        );
    }

    @Test
    @DisplayName("Admin number validated without exceptions")
    void admin_number_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> adminNumberValidator.validate(
                getNumber(),
                List.of(getAdminNumberWithId())
            )
        );
    }

    @Test
    @DisplayName("Sign up data validated without exceptions")
    void sign_up_data_validated_without_exceptions() {
        assertTrue(
            () -> signUpValidator.isValidData(
                getFirstName(),
                getLastName(),
                getEmail(),
                getPhone(),
                getPassword(),
                getNumber(),
                getUsers(),
                List.of(getAdminNumberWithId())
            )
        );
    }
}
