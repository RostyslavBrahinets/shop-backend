package com.shop.tablesschemas;

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
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/basket/V20220421161946__Create_table_basket.sql"
})
public class BasketTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "basket"
        );
    }

    @Test
    @DisplayName("Failed to insert null total cost value")
    void failed_to_insert_null_total_cost_value() {
        var params = new MapSqlParameterSource();
        params.addValue("total_cost", null);
        params.addValue("person_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO basket(total_cost, person_id) VALUES (:total_cost, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null person id value")
    void failed_to_insert_null_person_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("total_cost", 0);
        params.addValue("person_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO basket(total_cost, person_id) VALUES (:total_cost, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
