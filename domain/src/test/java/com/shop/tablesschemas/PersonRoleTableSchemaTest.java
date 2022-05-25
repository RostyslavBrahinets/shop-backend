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
    "classpath:db/migration/person_role/V20220505173243__Create_table_person_role.sql"
})
public class PersonRoleTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "person_role"
        );
    }

    @Test
    @DisplayName("Failed to insert null person id value")
    void failed_to_insert_null_person_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("person_id", null);
        params.addValue("role_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO person_role(person_id, role_id) "
                    + "VALUES (:person_id, :role_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null role id value")
    void failed_to_insert_null_role_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("person_id", 1);
        params.addValue("role_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO person_role(person_id, role_id) "
                    + "VALUES (:person_id, :role_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
