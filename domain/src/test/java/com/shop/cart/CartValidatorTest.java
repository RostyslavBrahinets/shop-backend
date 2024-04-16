package com.shop.cart;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.shop.cart.CartParameter.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CartValidatorTest {
    private CartValidator cartValidator;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartValidator = new CartValidator();
    }

    @Test
    @DisplayName("Total cost products in cart validated without exceptions")
    void total_cost_products_in_cart_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> cartValidator.validate(getPriceAmount())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because total cost less then zero")
    void throw_validation_exception_because_total_cost_less_then_zero() {
        assertThrows(
            ValidationException.class,
            () -> cartValidator.validate(-1.0)
        );
    }

    @Test
    @DisplayName("Id of cart validated without exceptions")
    void id_of_cart_validated_without_exceptions() {
        when(cartRepository.findAll())
            .thenReturn(
                List.of(getCartWithId())
            );

        assertDoesNotThrow(
            () -> cartValidator.validate(getPriceAmount())
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of cart not found")
    void throw_not_found_exception_because_id_of_cart_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> cartValidator.validate(getCartId(), getCarts())
        );
    }
}
