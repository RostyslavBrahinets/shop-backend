package com.shop.category;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.category.Category;
import com.shop.category.CategoryRepository;
import com.shop.category.CategoryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CategoryValidatorTest {
    private CategoryValidator categoryValidator;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryValidator = new CategoryValidator();
    }

    @Test
    @DisplayName("Category validated without exceptions")
    void category_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> categoryValidator.validateCategory("name")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name of category is null")
    void throw_validation_exception_because_name_of_category_is_null() {
        assertThrows(
            ValidationException.class,
            () -> categoryValidator.validateCategory(null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name of category is empty")
    void throw_validation_exception_because_name_of_category_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> categoryValidator.validateCategory("")
        );
    }

    @Test
    @DisplayName("Id of category validated without exceptions")
    void id_of_category_validated_without_exceptions() {
        List<Category> categories = List.of(
            Category.of(
                "name"
            ).withId(1)
        );

        when(categoryRepository.findAll())
            .thenReturn(
                List.of(new Category(1, "name"))
            );

        assertDoesNotThrow(
            () -> categoryValidator.validate(1, categories)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of category not found")
    void throw_not_found_exception_because_id_of_category_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> categoryValidator.validate(1, List.of())
        );
    }

    @Test
    @DisplayName("Name of category validated without exceptions")
    void name_of_category_validated_without_exceptions() {
        List<Category> categories = List.of(
            Category.of(
                "name"
            ).withId(1)
        );

        when(categoryRepository.findAll())
            .thenReturn(
                List.of(new Category(1, "name"))
            );

        assertDoesNotThrow(
            () -> categoryValidator.validate("name", categories)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because name of category not found")
    void throw_not_found_exception_because_name_of_category_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> categoryValidator.validate("name", List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name is null")
    void throw_validation_exception_because_name_is_null() {
        List<Category> categories = List.of(
            Category.of(
                "name"
            ).withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> categoryValidator.validate(null, categories)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because name is empty")
    void throw_validation_exception_because_name_is_empty() {
        List<Category> categories = List.of(
            Category.of(
                "name"
            ).withId(1)
        );

        assertThrows(
            ValidationException.class,
            () -> categoryValidator.validate("", categories)
        );
    }
}
