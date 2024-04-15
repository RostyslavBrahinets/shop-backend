package com.shop.product;

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

import static com.shop.product.ProductParameter.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProductValidatorTest {
    private ProductValidator productValidator;

    @Mock
    private ProductRepository productRepository;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productValidator = new ProductValidator();
        products = List.of(getProductWithId());
    }

    @Test
    @DisplayName("Product validated without exceptions")
    void product_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> productValidator.validate(
                getName(),
                getDescribe(),
                getPrice(),
                getBarcode(),
                getProducts()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid name of product")
    void throw_validation_exception_for_invalid_name_of_product(String name, List<Product> products) {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(
                name,
                getDescribe(),
                getPrice(),
                getBarcode(),
                products
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid describe of product")
    void throw_validation_exception_for_invalid_describe_of_product(String describe, List<Product> products) {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(
                getName(),
                describe,
                getPrice(),
                getBarcode(),
                products
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because price of product less then expected")
    void throw_validation_exception_because_price_of_product_less_then_expected() {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(
                getName(),
                getDescribe(),
                -1,
                getBarcode(),
                products
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid barcode of product")
    void throw_validation_exception_for_invalid_barcode_of_product(String barcode, List<Product> products) {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(
                getName(),
                getDescribe(),
                getPrice(),
                barcode,
                products
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because barcode of product already in use")
    void throw_validation_exception_because_barcode_of_product_already_in_use() {
        List<Product> products = List.of(getProductWithId());

        when(productRepository.findAll()).thenReturn(products);

        assertThrows(
            ValidationException.class,
            () -> productValidator.validate(
                getName(),
                getDescribe(),
                getPrice(),
                getBarcode(),
                products
            )
        );
    }

    @Test
    @DisplayName("Id of product validated without exceptions")
    void id_of_product_validated_without_exceptions() {
        List<Product> products = List.of(getProductWithId());

        when(productRepository.findAll()).thenReturn(products);

        assertDoesNotThrow(
            () -> productValidator.validate(getProductId(), products)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of product not found")
    void throw_not_found_exception_because_id_of_product_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> productValidator.validate(getProductId(), getProducts())
        );
    }

    @Test
    @DisplayName("Barcode validated without exceptions")
    void barcode_validated_without_exceptions() {
        List<Product> products = List.of(getProductWithId());

        when(productRepository.findAll()).thenReturn(products);

        assertDoesNotThrow(
            () -> productValidator.validateBarcode(getBarcode(), products)
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid barcode")
    void throw_validation_exception_for_invalid_barcode(String barcode, List<Product> products) {
        assertThrows(
            ValidationException.class,
            () -> productValidator.validateBarcode(barcode, products)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because barcode not found")
    void throw_not_found_exception_because_barcode_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> productValidator.validateBarcode(getBarcode(), getProducts())
        );
    }

    private static Stream<Arguments> validationTestCases() {
        List<Product> products = List.of(getProductWithId());

        return Stream.of(
            Arguments.of(null, products),
            Arguments.of("", products)
        );
    }
}
