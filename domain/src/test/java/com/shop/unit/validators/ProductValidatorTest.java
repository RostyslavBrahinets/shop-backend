package com.shop.unit.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Product;
import com.shop.repositories.ProductRepository;
import com.shop.validators.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ProductValidatorTest {
    private ProductValidator productValidator;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productValidator = new ProductValidator(productRepository);
    }

    @Test
    @DisplayName("Product validated without exceptions")
    void contact_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> productValidator.validate("name", "describe", 0, "123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name of product is null")
    void throw_validation_exception_because_name_of_product_is_null() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(null, "describe", 0, "123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name of product is empty")
    void throw_validation_exception_because_name_of_product_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("", "describe", 0, "123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because describe of product is null")
    void throw_validation_exception_because_describe_of_product_is_null() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", null, 0, "123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because describe of product is empty")
    void throw_validation_exception_because_describe_of_product_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "", 0, "123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because price of product less then expected")
    void throw_validation_exception_because_price_of_product_less_then_expected() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", -1, "123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode of product is null")
    void throw_validation_exception_because_barcode_of_product_is_null() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", 0, null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode of product is empty")
    void throw_validation_exception_because_barcode_of_product_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", 0, "")
        );
    }

    @Test
    @DisplayName("Id of product validated without exceptions")
    void id_of_product_validated_without_exceptions() {
        when(productRepository.findAll())
            .thenReturn(
                List.of(
                    new Product(
                        1,
                        "name",
                        "describe",
                        0,
                        "123",
                        true,
                        new byte[]{1, 1, 1}
                    )
                )
            );

        assertDoesNotThrow(
            () -> productValidator.validate(1)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of product not found")
    void throw_not_found_exception_because_id_of_product_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> productValidator.validate(1)
        );
    }

    @Test
    @DisplayName("Barcode validated without exceptions")
    void barcode_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> productValidator.validate("123")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode is null")
    void throw_validation_exception_because_barcode_is_null() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode is empty")
    void throw_validation_exception_because_barcode_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("")
        );
    }
}
