package com.shop.integration.tablesschemas;

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
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql"
})
public class PersonTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "person"
        );
    }

    @Test
    @DisplayName("Failed to insert null first name value")
    void failed_to_insert_null_first_name_value() {
        var params = new MapSqlParameterSource();
        params.addValue("first_name", null);
        params.addValue("last_name", "Smith");

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO person(first_name, last_name) "
                    + "VALUES (:first_name, :last_name)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null last name value")
    void failed_to_insert_null_last_name_value() {
        var params = new MapSqlParameterSource();
        params.addValue("first_name", "John");
        params.addValue("last_name", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO person(first_name, last_name) "
                    + "VALUES (:first_name, :last_name)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
