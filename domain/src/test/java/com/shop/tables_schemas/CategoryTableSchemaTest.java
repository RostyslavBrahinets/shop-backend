package com.shop.tables_schemas;

import com.shop.configs.DatabaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThatCode;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/category/V20220421162204__Create_table_category.sql"
})
public class CategoryTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "category"
        );
    }

    @Test
    @DisplayName("Failed to insert null name value")
    void failed_to_insert_null_name_value() {
        var params = new MapSqlParameterSource();
        params.addValue("name", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO category(name) VALUES (:name)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
