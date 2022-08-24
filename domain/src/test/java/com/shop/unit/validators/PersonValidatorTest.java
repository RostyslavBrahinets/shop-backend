package com.shop.unit.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Person;
import com.shop.repositories.PersonRepository;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PersonValidatorTest {
    private PersonValidator personValidator;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personValidator = new PersonValidator();
    }

    @Test
    @DisplayName("Person validated without exceptions")
    void person_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> personValidator.validate("John", "Smith")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because first name of person is null")
    void throw_validation_exception_because_first_name_of_person_is_null() {
        assertThrows(
            ValidationException.class,
            () -> personValidator.validate(null, "Smith")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because first name of person is empty")
    void throw_validation_exception_because_first_name_of_person_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> personValidator.validate("", "Smith")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because last name of person is null")
    void throw_validation_exception_because_last_name_of_person_is_null() {
        assertThrows(
            ValidationException.class,
            () -> personValidator.validate("John", null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because last name of person is empty")
    void throw_validation_exception_because_last_name_of_person_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> personValidator.validate("John", "")
        );
    }

    @Test
    @DisplayName("Id of person validated without exceptions")
    void id_of_person_validated_without_exceptions() {
        List<Person> people = List.of(new Person(1, "John", "Smith"));

        when(personRepository.findAll()).thenReturn(people);

        assertDoesNotThrow(() -> personValidator.validate(1, people));
    }

    @Test
    @DisplayName("Throw NotFoundException because id of person not found")
    void throw_not_found_exception_because_id_of_person_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> personValidator.validate(1, List.of())
        );
    }
}
