package com.shop.adminnumber;

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

import static com.shop.SqlMigrationClasspath.ADMIN_NUMBER;
import static org.assertj.core.api.Assertions.assertThatCode;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    ADMIN_NUMBER
})
class AdminNumberTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "admin_number"
        );
    }

    @Test
    @DisplayName("Failed to insert null number value")
    void failed_to_insert_null_number_value() {
        var params = new MapSqlParameterSource();
        params.addValue("number", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO admin_number(number) VALUES (:number)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
