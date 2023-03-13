package com.shop.unit.validators;

import com.shop.models.AdminNumber;
import com.shop.validators.AdminNumberValidator;
import com.shop.validators.RegistrationValidator;
import com.shop.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationValidatorTest {
    private RegistrationValidator registrationValidator;
    private UserValidator userValidator;
    private AdminNumberValidator adminNumberValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userValidator = new UserValidator();
        adminNumberValidator = new AdminNumberValidator();

        registrationValidator = new RegistrationValidator(
            userValidator,
            adminNumberValidator
        );
    }

    @Test
    @DisplayName("Registration data validated without exceptions")
    void registration_data_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                "password",
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
            () -> registrationValidator.isValidData(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                "password",
                "12345678",
                List.of(),
                List.of(AdminNumber.of("12345678").withId(1))
            )
        );
    }
}
