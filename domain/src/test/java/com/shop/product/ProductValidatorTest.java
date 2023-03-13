package com.shop.product;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.product.Product;
import com.shop.product.ProductRepository;
import com.shop.product.ProductValidator;
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

        productValidator = new ProductValidator();
    }

    @Test
    @DisplayName("Product validated without exceptions")
    void product_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> productValidator.validate("name", "describe", 0, "123", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name of product is null")
    void throw_validation_exception_because_name_of_product_is_null() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(null, "describe", 0, "123", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name of product is empty")
    void throw_validation_exception_because_name_of_product_is_empty() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("", "describe", 0, "123", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because describe of product is null")
    void throw_validation_exception_because_describe_of_product_is_null() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", null, 0, "123", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because describe of product is empty")
    void throw_validation_exception_because_describe_of_product_is_empty() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "", 0, "123", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because price of product less then expected")
    void throw_validation_exception_because_price_of_product_less_then_expected() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", -1, "123", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode of product is null")
    void throw_validation_exception_because_barcode_of_product_is_null() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", 0, null, products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode of product is empty")
    void throw_validation_exception_because_barcode_of_product_is_empty() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", 0, "", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode of product already in use")
    void throw_validation_exception_because_barcode_of_product_already_in_use() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        when(productRepository.findAll()).thenReturn(products);

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("name", "describe", 0, "123", products)
        );
    }

    @Test
    @DisplayName("Id of product validated without exceptions")
    void id_of_product_validated_without_exceptions() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        when(productRepository.findAll()).thenReturn(products);

        assertDoesNotThrow(
            () -> productValidator.validate(1, products)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of product not found")
    void throw_not_found_exception_because_id_of_product_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> productValidator.validate(1, List.of())
        );
    }

    @Test
    @DisplayName("Barcode validated without exceptions")
    void barcode_validated_without_exceptions() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        when(productRepository.findAll()).thenReturn(products);

        assertDoesNotThrow(
            () -> productValidator.validate("123", products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode is null")
    void throw_validation_exception_because_barcode_is_null() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(null, products)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode is empty")
    void throw_validation_exception_because_barcode_is_empty() {
        List<Product> products = List.of(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate("", products)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because barcode not found")
    void throw_not_found_exception_because_barcode_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> productValidator.validate("123", List.of())
        );
    }
}
