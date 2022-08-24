package com.shop.unit.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import com.shop.validators.ContactValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ContactValidatorTest {
    private ContactValidator contactValidator;

    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        contactValidator = new ContactValidator();
    }

    @Test
    @DisplayName("Contact validated without exceptions")
    void contact_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> contactValidator.validate("test@email.com", "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact is null")
    void throw_validation_exception_because_email_of_contact_is_null() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate(null, "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact is empty")
    void throw_validation_exception_because_email_of_contact_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("", "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact starts with char '@'")
    void throw_validation_exception_because_email_of_contact_starts_with_char_at() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("@email.com", "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact not contains char '@'")
    void throw_validation_exception_because_email_of_contact_not_contains_char_at() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test.email.com", "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact not ends '.com'")
    void throw_validation_exception_because_email_of_contact_not_ends_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email", "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact ends '@.com'")
    void throw_validation_exception_because_email_of_contact_ends_char_at_and_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@.com", "+380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of contact already in use")
    void throw_validation_exception_because_email_of_contact_already_in_use() {
        List<Contact> contacts = List.of(
            new Contact(
                1,
                "test@email.com",
                "+380000000000",
                "password"
            )
        );

        when(contactRepository.findAll()).thenReturn(contacts);

        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", "+380000000000", "password", contacts)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of contact is null")
    void throw_validation_exception_because_phone_of_contact_is_null() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", null, "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of contact is empty")
    void throw_validation_exception_because_phone_of_contact_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", "", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of contact not starts with '+'")
    void throw_validation_exception_because_phone_of_contact_not_starts_with_plus() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", "380000000000", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because length of phone of contact less then expected")
    void throw_validation_exception_because_phone_of_contact_less_then_expected() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", "+380", "password", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of contact is already in use")
    void throw_validation_exception_because_phone_of_contact_already_in_use() {
        when(contactRepository.findAll())
            .thenReturn(
                List.of(new Contact(1, "test@email.com", "+380000000000", "password"))
            );

        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("+380000000000")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because password of contact is null")
    void throw_validation_exception_because_password_of_contact_is_null() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", "+380000000000", null, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because password of contact is empty")
    void throw_validation_exception_because_password_of_contact_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email.com", "+380000000000", "", List.of())
        );
    }

    @Test
    @DisplayName("Phone validated without exceptions")
    void phone_validated_without_exceptions() {
        Contact contact = Contact.of(
            "test@email.com",
            "+380000000000",
            "password"
        ).withId(1);

        when(contactRepository.findById(1))
            .thenReturn(Optional.of(
                contact
            ));

        assertDoesNotThrow(
            () -> contactValidator.validatePhone("+380000000000", contact, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone is null")
    void throw_validation_exception_because_phone_is_null() {
        Contact contact = Contact.of(
            "test@email.com",
            null,
            "password"
        ).withId(1);

        when(contactRepository.findById(1))
            .thenReturn(Optional.of(
                contact
            ));

        assertThrows(
            ValidationException.class,
            () -> contactValidator.validatePhone(null, contact, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone is empty")
    void throw_validation_exception_because_phone_is_empty() {
        Contact contact = Contact.of(
            "test@email.com",
            "",
            "password"
        ).withId(1);

        when(contactRepository.findById(1))
            .thenReturn(Optional.of(
                contact
            ));

        assertThrows(
            ValidationException.class,
            () -> contactValidator.validatePhone("", contact, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone not starts with '+'")
    void throw_validation_exception_because_phone_not_starts_with_plus() {
        Contact contact = Contact.of(
            "test@email.com",
            "380000000000",
            "password"
        ).withId(1);

        when(contactRepository.findById(1))
            .thenReturn(Optional.of(
                contact
            ));

        assertThrows(
            ValidationException.class,
            () -> contactValidator.validatePhone("380000000000", contact, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because length of phone less then expected")
    void throw_validation_exception_because_length_of_phone_less_then_expected() {
        Contact contact = Contact.of(
            "test@email.com",
            "+380",
            "password"
        ).withId(1);

        when(contactRepository.findById(1))
            .thenReturn(Optional.of(
                contact
            ));

        assertThrows(
            ValidationException.class,
            () -> contactValidator.validatePhone("+380", contact, List.of())
        );
    }

    @Test
    @DisplayName("Id of contact validated without exceptions")
    void id_of_contact_validated_without_exceptions() {
        List<Contact> contacts = List.of(new Contact(1, "test@email.com", "+380000000000", "password"));
        when(contactRepository.findAll()).thenReturn(contacts);

        assertDoesNotThrow(
            () -> contactValidator.validate(1, contacts)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of contact not found")
    void throw_not_found_exception_because_id_of_contact_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> contactValidator.validate(1, List.of())
        );
    }

    @Test
    @DisplayName("Email of contact validated without exceptions")
    void email_of_contact_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> contactValidator.validate("test@email.com")
        );
    }

    @Test
    @DisplayName("Email of admin validated without exceptions")
    void email_of_admin_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> contactValidator.validate("admin")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email is null")
    void throw_validation_exception_because_email_is_null() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate(null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email is empty")
    void throw_validation_exception_because_email_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email starts with char '@'")
    void throw_validation_exception_because_email_starts_with_char_at() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("@email.com")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email not contains char '@'")
    void throw_validation_exception_because_email_not_contains_char_at() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test.email.com")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email not ends '.com'")
    void throw_validation_exception_because_email_not_ends_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@email")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email ends '@.com'")
    void throw_validation_exception_because_email_ends_char_at_and_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> contactValidator.validate("test@.com")
        );
    }
}
