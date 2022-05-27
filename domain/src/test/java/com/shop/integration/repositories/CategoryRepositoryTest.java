package com.shop.integration.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.CategoryDao;
import com.shop.models.Category;
import com.shop.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    CategoryRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/category/V20220421162204__Create_table_category.sql"
})
public class CategoryRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE category");
    }

    @Test
    @DisplayName("No categories in database")
    void no_history_records_in_db() {
        int categoriesCount = categoryRepository.count();

        assertThat(categoriesCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing category")
    void nothing_happened_when_trying_to_delete_not_existing_category() {
        assertThatCode(() -> categoryRepository.delete("category"))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Category was deleted")
    @DirtiesContext
    void category_was_deleted() {
        var categoryToSave = Category.of("name");

        categoryRepository.save("name");

        assertThat(categoryRepository.count()).isEqualTo(1);

        categoryRepository.delete(categoryToSave.getName());

        assertThat(categoryRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save category and check category data")
    @DirtiesContext
    void save_category_and_check_category_data() {
        categoryRepository.save("name");
        var savedCategory = categoryRepository.findById(categoryRepository.count());
        Category category = null;
        if (savedCategory.isPresent()) {
            category = savedCategory.get();
        }

        assertThat(category).extracting(Category::getId).isEqualTo(1L);
        assertThat(category).extracting(Category::getName).isEqualTo("name");
    }

    @Test
    @DisplayName("Save multiple categories")
    @DirtiesContext
    void save_multiple_categories() {
        categoryRepository.save("name1");
        categoryRepository.save("name2");

        assertThat(categoryRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Category was not found")
    void category_was_not_found() {
        Optional<Category> category = categoryRepository.findById(1);

        assertThat(category).isEmpty();
    }

    @Test
    @DisplayName("Category was found")
    @DirtiesContext
    void category_was_found() {
        categoryRepository.save("name");

        Optional<Category> category = categoryRepository.findById(categoryRepository.count());

        assertThat(category).get().isEqualTo(Category.of("name").withId(1));
    }

    @Test
    @DisplayName("Find all categories")
    @DirtiesContext
    void find_all_categories() {
        categoryRepository.save("name1");
        categoryRepository.save("name2");

        var categories = categoryRepository.findAll();

        assertThat(categories).isEqualTo(
            List.of(
                Category.of("name1").withId(1),
                Category.of("name2").withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public CategoryDao categoryDao() {
            return new CategoryDao(namedParameterJdbcTemplate);
        }

        @Bean
        public CategoryRepository categoryRepository(CategoryDao categoryDao) {
            return new CategoryRepository(categoryDao);
        }
    }
}
