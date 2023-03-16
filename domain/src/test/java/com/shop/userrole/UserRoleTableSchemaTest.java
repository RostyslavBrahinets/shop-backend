package com.shop.userrole;

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
    "classpath:db/migration/userrole/V20220505173244__Create_table_user_role.sql"
})
public class UserRoleTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "user_role"
        );
    }

    @Test
    @DisplayName("Failed to insert null user id value")
    void failed_to_insert_null_user_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("user_id", null);
        params.addValue("role_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO user_role(user_id, role_id) "
                    + "VALUES (:user_id, :role_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null role id value")
    void failed_to_insert_null_role_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("user_id", 1);
        params.addValue("role_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO user_role(user_id, role_id) "
                    + "VALUES (:user_id, :role_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
