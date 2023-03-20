package com.shop.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private CategoryService categoryService;

    private List<Category> categories;

    @BeforeEach
    void setUp() {
        categories = List.of();
    }

    @Test
    @DisplayName("Get all categories")
    void get_all_categories() {
        categoryService.findAll();

        verify(categoryRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get category by id")
    void get_category_by_id() {
        long id = 1;

        categoryService.findById(id);

        verify(categoryValidator, atLeast(1)).validate(id, categories);
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Get category by name")
    void get_category_by_name() {
        String name = "name";

        categoryService.findByName(name);

        verify(categoryValidator, atLeast(1)).validate(name, categories);
        verify(categoryRepository).findByName(name);
    }

    @Test
    @DisplayName("Save category")
    void save_category() {
        String name = "name";

        categoryService.save(Category.of(name));

        verify(categoryValidator, atLeast(1)).validateCategory(name);
        verify(categoryRepository).save(name);
    }

    @Test
    @DisplayName("Delete category by name")
    void delete_category_by_name() {
        String name = "name";

        categoryService.delete(Category.of(name));

        verify(categoryValidator, atLeast(1)).validate(name, categories);
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
