package com.shop.unit.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
import com.shop.validators.BasketValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BasketValidatorTest {
    private BasketValidator basketValidator;

    @Mock
    private BasketRepository basketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        basketValidator = new BasketValidator();
    }

    @Test
    @DisplayName("Total cost products in basket validated without exceptions")
    void total_cost_products_in_basket_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> basketValidator.validate(0.0)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because total cost less then zero")
    void throw_validation_exception_because_total_cost_less_then_zero() {
        assertThrows(
            ValidationException.class,
            () -> basketValidator.validate(-1.0)
        );
    }

    @Test
    @DisplayName("Id of basket validated without exceptions")
    void id_of_basket_validated_without_exceptions() {
        when(basketRepository.findAll())
            .thenReturn(
                List.of(new Basket(1, 0))
            );

        assertDoesNotThrow(
            () -> basketValidator.validate(1)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of basket not found")
    void throw_not_found_exception_because_id_of_basket_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> basketValidator.validate(1, List.of())
        );
    }
}
