package com.shop.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryValidator categoryValidator;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryService = new CategoryService(
            categoryRepository,
            categoryValidator
        );
    }

    @Test
    @DisplayName("Category was saved for with correct input")
    void category_was_saved_with_correct_input() {
        Category savedCategory = categoryService.save(Category.of("name"));

        verify(categoryRepository).save(Category.of("name").withId(1));

        assertThat(savedCategory).isEqualTo(new Category(1, "name"));
    }

    @Test
    @DisplayName("Empty list of categories is returned in case when no categories in storage")
    void empty_list_of_categories_is_returned_in_case_when_no_categories_in_storage() {
        when(categoryRepository.findAll()).thenReturn(emptyList());

        List<Category> categories = categoryService.findAll();

        assertThat(categories).isEmpty();
    }

    @Test
    @DisplayName("List of categories is returned in case when categories are exists in storage")
    void list_of_categories_is_returned_in_case_when_categories_are_exists_in_storage() {
        when(categoryRepository.findAll()).thenReturn(
            List.of(
                Category.of("name").withId(1)
            )
        );

        List<Category> categories = categoryService.findAll();

        assertThat(categories).isEqualTo(List.of(new Category(1, "name")));
    }

    @Test
    @DisplayName("Category was found by id")
    void category_was_found_by_id() {
        when(categoryRepository.findById(1)).thenReturn(
            Optional.of(Category.of("name").withId(1))
        );

        Category category = categoryService.findById(1);

        assertThat(category).isEqualTo(new Category(1, "name"));
    }

    @Test
    @DisplayName("Category was found by user")
    void category_was_found_by_user() {
        when(categoryRepository.findByName("name")).thenReturn(
            Optional.of(Category.of("name").withId(1))
        );

        Category category = categoryService.findByName("name");

        assertThat(category).isEqualTo(new Category(1, "name"));
    }

    @Test
    @DisplayName("Category was deleted")
    void category_was_deleted() {
        categoryService.delete(Category.of("name"));
        verify(categoryRepository).delete(Category.of("name"));
    }
}
