package com.shop.services;

import com.shop.models.Category;
import com.shop.repositories.CategoryRepository;
import com.shop.validators.CategoryValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        CategoryService.class,
        CategoryServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class CategoryServiceContextConfigurationTest {
    @Autowired
    private Category category;
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

        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Get category by id")
    void get_category_by_id() {
        long id = 1;

        categoryService.findById(id);

        verify(categoryValidator, atLeast(1)).validate(id);
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Save category")
    void save_category() {
        categoryService.save(category);

        verify(categoryValidator, atLeast(1)).validate(category);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Delete category by name")
    void delete_category_by_name() {
        String name = "name";

        categoryService.delete(name);

        verify(categoryValidator, atLeast(1)).validate(name);
        verify(categoryRepository).delete(name);
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
    }
}
