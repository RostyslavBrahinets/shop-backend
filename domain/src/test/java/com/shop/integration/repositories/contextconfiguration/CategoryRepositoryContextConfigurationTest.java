package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.CategoryDao;
import com.shop.models.Category;
import com.shop.repositories.CategoryRepository;
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
        CategoryRepository.class,
        CategoryRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class CategoryRepositoryContextConfigurationTest {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Get all categories")
    void get_all_categories() {
        categoryRepository.findAll();

        verify(categoryDao, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get category by id")
    void get_category_by_id() {
        long id = 1;

        categoryRepository.findById(id);

        verify(categoryDao).findById(id);
    }

    @Test
    @DisplayName("Get category by name")
    void get_category_by_name() {
        String name = "name";

        categoryRepository.findByName(name);

        verify(categoryDao).findByName(name);
    }

    @Test
    @DisplayName("Save category")
    void save_category() {
        String name = "name";

        categoryRepository.save(name);

        verify(categoryDao).save(name);
    }

    @Test
    @DisplayName("Delete category")
    void delete_category() {
        String name = "name";

        categoryRepository.delete(name);

        verify(categoryDao).delete(name);
    }

    @Test
    @DisplayName("Count categories")
    void count_categories() {
        categoryRepository.count();

        verify(categoryDao, atLeast(1)).findAll();
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Category category() {
            return mock(Category.class);
        }

        @Bean
        public CategoryDao categoryDao() {
            return mock(CategoryDao.class);
        }
    }
}
