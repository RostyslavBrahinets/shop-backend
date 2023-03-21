package com.shop.signup;

import com.shop.adminnumber.AdminNumber;
import com.shop.adminnumber.AdminNumberValidator;
import com.shop.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignUpValidatorTest {
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
    @DisplayName("Sign up data validated without exceptions")
    void sign_up_data_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );

        assertDoesNotThrow(
            () -> adminNumberValidator.validate(
                "12345678",
                List.of(AdminNumber.of("12345678").withId(1))
            )
        );

        assertTrue(
            () -> signUpValidator.isValidData(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "12345678",
                List.of(),
                List.of(AdminNumber.of("12345678").withId(1))
            )
        );
    }
}
