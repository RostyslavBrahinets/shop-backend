package com.shop.adminnumber;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Stream;

import static com.shop.adminnumber.AdminNumberParameter.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AdminNumberValidatorTest {
    private AdminNumberValidator adminNumberValidator;

    @Mock
    private AdminNumberRepository adminNumberRepository;
    private static List<AdminNumber> adminNumbers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminNumberValidator = new AdminNumberValidator();
        adminNumbers = List.of(getAdminNumberWithId());
    }

    @Test
    @DisplayName("Number of admin validated without exceptions")
    void number_of_admin_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> adminNumberValidator.validateAdminNumber(getNumber())
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
        when(adminNumberRepository.findAll())
            .thenReturn(
                adminNumbers
            );

        assertDoesNotThrow(
            () -> adminNumberValidator.validate(getAdminNumberId(), adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of number of admin not found")
    void throw_not_found_exception_because_id_of_number_of_admin_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> adminNumberValidator.validate(getAdminNumberId(), getAdminNumbers())
        );
    }

    @Test
    @DisplayName("Number of number of admin validated without exceptions")
    void number_of_number_of_admin_validated_without_exceptions() {
        when(adminNumberRepository.findAll())
            .thenReturn(
                adminNumbers
            );

        assertDoesNotThrow(
            () -> adminNumberValidator.validate(getNumber(), adminNumbers)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because number of admin not found")
    void throw_not_found_exception_because_number_of_admin_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> adminNumberValidator.validate(getNumber(), getAdminNumbers())
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid number")
    void throw_validation_exception_for_invalid_number(String number, List<AdminNumber> adminNumbers) {
        assertThrows(ValidationException.class, () -> adminNumberValidator.validate(number, adminNumbers));
    }

    private static Stream<Arguments> validationTestCases() {
        return Stream.of(
            Arguments.of(null, adminNumbers),
            Arguments.of("", adminNumbers),
            Arguments.of("1234", adminNumbers),
            Arguments.of("123456789", adminNumbers)
        );
    }
}
