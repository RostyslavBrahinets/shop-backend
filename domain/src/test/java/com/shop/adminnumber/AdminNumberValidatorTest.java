package com.shop.adminnumber;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AdminNumberValidatorTest {
    private AdminNumberValidator adminNumberValidator;

    @Mock
    private AdminNumberRepository adminNumberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminNumberValidator = new AdminNumberValidator();
    }

    @Test
    @DisplayName("Number of admin validated without exceptions")
    void number_of_admin_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> adminNumberValidator.validateAdminNumber("12345678")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number of admin is null")
    void throw_validation_exception_because_number_of_admin_is_null() {
        assertThrows(
            ValidationException.class,
            () -> adminNumberValidator.validateAdminNumber(null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number of admin is empty")
    void throw_validation_exception_because_number_of_admin_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> adminNumberValidator.validateAdminNumber("")
        );
    }

    @Test
    @DisplayName("Id of number of admin validated without exceptions")
    void id_of_number_of_admin_validated_without_exceptions() {
        List<AdminNumber> adminNumbers = List.of(
            AdminNumber.of(
                "12345678"
            ).withId(1)
        );

        when(adminNumberRepository.findAll())
            .thenReturn(
                List.of(new AdminNumber(1, "12345678"))
            );

        assertDoesNotThrow(
            () -> adminNumberValidator.validate(1, adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of number of admin not found")
    void throw_not_found_exception_because_id_of_number_of_admin_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> adminNumberValidator.validate(1, List.of())
        );
    }

    @Test
    @DisplayName("Number of number of admin validated without exceptions")
    void number_of_number_of_admin_validated_without_exceptions() {
        List<AdminNumber> adminNumbers = List.of(
            AdminNumber.of(
                "12345678"
            ).withId(1)
        );

        when(adminNumberRepository.findAll())
            .thenReturn(
                List.of(new AdminNumber(1, "12345678"))
            );

        assertDoesNotThrow(
            () -> adminNumberValidator.validate("12345678", adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because number of admin not found")
    void throw_not_found_exception_because_number_of_admin_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> adminNumberValidator.validate("12345678", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number is null")
    void throw_validation_exception_because_number_is_null() {
        List<AdminNumber> adminNumbers = List.of(
            AdminNumber.of(
                "12345678"
            ).withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> adminNumberValidator.validate(null, adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number is empty")
    void throw_validation_exception_because_number_is_empty() {
        List<AdminNumber> adminNumbers = List.of(
            AdminNumber.of(
                "12345678"
            ).withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> adminNumberValidator.validate("", adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number is short")
    void throw_validation_exception_because_number_is_short() {
        List<AdminNumber> adminNumbers = List.of(
            AdminNumber.of(
                "12345678"
            ).withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> adminNumberValidator.validate("1234", adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number is long")
    void throw_validation_exception_because_number_is_long() {
        List<AdminNumber> adminNumbers = List.of(
            AdminNumber.of(
                "12345678"
            ).withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> adminNumberValidator.validate("123456789", adminNumbers)
        );
    }
}
