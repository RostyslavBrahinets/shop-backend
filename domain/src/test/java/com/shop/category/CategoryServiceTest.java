package com.shop.category;

import com.shop.productcategory.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.shop.category.CategoryParameter.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryValidator categoryValidator;
    @Mock
    private ProductCategoryRepository productCategoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryService = new CategoryService(
            categoryRepository,
            categoryValidator,
            productCategoryRepository
        );
    }

    @Test
    @DisplayName("Empty list of categories is returned in case when no categories in storage")
    void empty_list_of_categories_is_returned_in_case_when_no_categories_in_storage() {
        when(categoryRepository.findAll()).thenReturn(emptyList());

        List<Category> categories = categoryService.findAll();

        verify(categoryRepository).findAll();

        assertThat(categories).isEmpty();
    }

    @Test
    @DisplayName("List of categories is returned in case when categories are exists in storage")
    void list_of_categories_is_returned_in_case_when_categories_are_exists_in_storage() {
        when(categoryRepository.findAll()).thenReturn(
            List.of(getCategoryWithId())
        );

        List<Category> categories = categoryService.findAll();

        verify(categoryRepository).findAll();

        assertThat(categories).isEqualTo(List.of(getCategoryWithId()));
    }

    @Test
    @DisplayName("Category was found by id")
    void category_was_found_by_id() {
        when(categoryRepository.findById(getCategoryId()))
            .thenReturn(
                Optional.of(getCategoryWithId())
            );

        Category category = categoryService.findById(getCategoryId());

        verify(categoryRepository).findAll();
        verify(categoryValidator).validate(getCategoryId(), getCategories());
        verify(categoryRepository).findById(getCategoryId());

        assertThat(category).isEqualTo(getCategoryWithId());
    }

    @Test
    @DisplayName("Category was saved for with correct input")
    void category_was_saved_with_correct_input() {
        Category savedCategory = categoryService.save(getCategoryWithoutId());

        verify(categoryValidator).validateCategory(getName());
        verify(categoryRepository).save(getCategoryWithId());
        verify(categoryRepository).findAll();

        assertThat(savedCategory).isEqualTo(getCategoryWithId());
    }

    @Test
    @DisplayName("Category was updated for with correct input")
    void category_was_updated_with_correct_input() {
        Category updatedCategory = categoryService.update(
            getCategoryId(),
            getCategoryWithoutId(getName2())
        );

        verify(categoryRepository).findAll();
        verify(categoryValidator).validate(getCategoryId(), getCategories());
        verify(categoryValidator).validateCategory(getName2());
        verify(categoryRepository).update(
            getCategoryId(),
            getCategoryWithId(getCategoryId(), getName2())
        );

        assertThat(updatedCategory).isEqualTo(getCategoryWithId(getCategoryId(), getName2()));
    }

    @Test
    @DisplayName("Category was deleted")
    void category_was_deleted() {
        categoryService.delete(getCategoryWithoutId());

        verify(categoryRepository).findAll();
        verify(categoryValidator).validate(getName(), getCategories());
        verify(categoryRepository).findByName(getName());
        verify(categoryRepository).delete(getCategoryWithoutId());
    }

    @Test
    @DisplayName("Category was found by user")
    void category_was_found_by_user() {
        when(categoryRepository.findByName(getName())).thenReturn(
            Optional.of(getCategoryWithId())
        );

        Category category = categoryService.findByName(getName());

        verify(categoryRepository).findAll();
        verify(categoryValidator).validate(getName(), getCategories());
        verify(categoryRepository).findByName(getName());

        assertThat(category).isEqualTo(getCategoryWithId());
    }
}
