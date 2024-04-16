package com.shop.category;

import com.shop.productcategory.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.shop.category.CategoryParameter.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        CategoryService.class,
        CategoryServiceContextConfigurationTest.TestContextConfig.class
    }
)
class CategoryServiceContextConfigurationTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("Get all categories")
    void get_all_categories() {
        categoryService.findAll();

        verify(categoryRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get category by id")
    void get_category_by_id() {
        categoryService.findById(getCategoryId());

        verify(categoryValidator, atLeast(1)).validate(getCategoryId(), getCategories());
        verify(categoryRepository).findById(getCategoryId());
    }

    @Test
    @DisplayName("Get category by name")
    void get_category_by_name() {
        categoryService.findByName(getName());

        verify(categoryValidator, atLeast(1)).validate(getName(), getCategories());
        verify(categoryRepository).findByName(getName());
    }

    @Test
    @DisplayName("Save category")
    void save_category() {
        categoryService.save(getCategoryWithoutId());

        verify(categoryValidator, atLeast(1)).validateCategory(getName());
        verify(categoryRepository).save(getCategoryWithId());
    }

    @Test
    @DisplayName("Delete category by name")
    void delete_category_by_name() {
        categoryService.delete(getCategoryWithoutId());

        verify(categoryValidator, atLeast(1)).validate(getName(), getCategories());
        verify(categoryRepository).delete(getCategoryWithoutId());
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Category category() {
            return mock(Category.class);
        }

        @Bean
        public CategoryRepository categoryRepository() {
            return mock(CategoryRepository.class);
        }

        @Bean
        public CategoryValidator categoryValidator() {
            return mock(CategoryValidator.class);
        }

        @Bean
        public ProductCategoryRepository productCategoryRepository() {
            return mock(ProductCategoryRepository.class);
        }
    }
}
