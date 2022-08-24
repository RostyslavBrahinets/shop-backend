package com.shop.unit.validators;

import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import com.shop.repositories.PersonRepository;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.RegistrationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationValidatorTest {
    private RegistrationValidator registrationValidator;
    private PersonValidator personValidator;
    private ContactValidator contactValidator;

    @Mock
    private PersonRepository personRepository;
    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personValidator = new PersonValidator();
        contactValidator = new ContactValidator();

        registrationValidator = new RegistrationValidator(
            personValidator,
            contactValidator
        );
    }

    @Test
    @DisplayName("Registration data validated without exceptions")
    void registration_data_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> personValidator.validate("John", "Smith")
        );

        assertDoesNotThrow(
            () -> contactValidator.validate(
                "test@email.com",
                "+380000000000",
                "password",
                List.of()
            )
        );

        assertTrue(
            () -> registrationValidator.isValidData(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                "password",
                List.of()
            )
        );
    }
}
