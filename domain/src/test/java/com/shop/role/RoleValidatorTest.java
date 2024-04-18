package com.shop.role;

import com.shop.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.shop.role.RoleParameter.getRoleId;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleValidatorTest {
    private RoleValidator roleValidator;

    @BeforeEach
    void setUp() {
        roleValidator = new RoleValidator();
    }

    @Test
    @DisplayName("Id of role validated without exceptions")
    void id_of_role_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> roleValidator.validate(getRoleId())
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of role less then expected")
    void throw_not_found_exception_because_id_of_role_less_then_expected() {
        assertThrows(
            NotFoundException.class,
            () -> roleValidator.validate(0)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of role more then expected")
    void throw_not_found_exception_because_id_of_role_more_then_expected() {
        assertThrows(
            NotFoundException.class,
            () -> roleValidator.validate(3)
        );
    }
}
