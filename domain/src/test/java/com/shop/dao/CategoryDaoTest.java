package com.shop.dao;

import com.shop.configs.DatabaseConfig;
import com.shop.models.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/category/V20220421162204__Create_table_category.sql"
})
public class CategoryDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private CategoryDao categoryDao;

    @BeforeEach
    void setUp() {
        categoryDao = new CategoryDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "category"
        );
    }

    private int fetchCategoriesCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "category"
        );
    }

    @Test
    @DisplayName("Save category")
    void save_category() {
        categoryDao.save("name");

        var categoriesCount = fetchCategoriesCount();

        assertThat(categoriesCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple categories")
    void save_multiple_categories() {
        categoryDao.save("name1");
        categoryDao.save("name2");

        var categoriesCount = fetchCategoriesCount();

        assertThat(categoriesCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Category by id was not found")
    void category_by_id_was_not_found() {
        Optional<Category> category = categoryDao.findById(1);

        assertThat(category).isEmpty();
    }

    @Test
    @DisplayName("Category by id was found")
    void category_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(Map.ofEntries(Map.entry("name", "name")));

        Optional<Category> category = categoryDao.findById(1);

        assertThat(category).get().isEqualTo(Category.of("name").withId(1));
    }

    @Test
    @DisplayName("Category by name was not found")
    void category_by_name_was_not_found() {
        Optional<Category> category = categoryDao.findByName("name");

        assertThat(category).isEmpty();
    }

    @Test
    @DisplayName("Category by name was found")
    void category_by_name_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(Map.ofEntries(Map.entry("name", "name")));

        Optional<Category> category = categoryDao.findByName("name");

        assertThat(category).get().isEqualTo(Category.of("name").withId(1));
    }

    @Test
    @DisplayName("Find all categories")
    void find_all_categories() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(Map.entry("name", "name1")),
            Map.ofEntries(Map.entry("name", "name2"))
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .executeBatch(batchInsertParameters);

        List<Category> categories = categoryDao.findAll();

        assertThat(categories).isEqualTo(
            List.of(
                Category.of("name1").withId(1),
                Category.of("name2").withId(2)
            )
        );
    }

    @Test
    @DisplayName("Category not deleted in case when not exists")
    void category_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> categoryDao.delete("name")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Category deleted")
    void category_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(Map.ofEntries(Map.entry("name", "name")));

        var categoriesCountBeforeDeletion = fetchCategoriesCount();

        assertThat(categoriesCountBeforeDeletion).isEqualTo(1);

        categoryDao.delete("name");

        var categoriesCount = fetchCategoriesCount();

        assertThat(categoriesCount).isZero();
    }
}
