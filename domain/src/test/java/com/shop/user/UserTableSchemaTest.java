package com.shop.user;

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
    "classpath:db/migration/adminnumber/V20220421160504__Create_table_admin_number.sql",
    "classpath:db/migration/user/V20220421161642__Create_table_user.sql"
})
class UserTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "admin_number, \"user\""
        );
    }

    @Test
    @DisplayName("Failed to insert null first name value")
    void failed_to_insert_null_first_name_value() {
        var params = new MapSqlParameterSource();
        params.addValue("first_name", null);
        params.addValue("last_name", "Smith");
        params.addValue("email", "test@email.com");
        params.addValue("phone", "+380000000000");
        params.addValue("password", "password");
        params.addValue("admin_number", "12345678");

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO \"user\"(first_name, last_name, email, phone, password, admin_number) "
                    + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number)",
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
        params.addValue("email", "test@email.com");
        params.addValue("phone", "+380000000000");
        params.addValue("password", "password");
        params.addValue("admin_number", "12345678");

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO \"user\"(first_name, last_name, email, phone, password, admin_number) "
                    + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null email value")
    void failed_to_insert_null_email_value() {
        var params = new MapSqlParameterSource();
        params.addValue("first_name", "John");
        params.addValue("last_name", "Smith");
        params.addValue("email", null);
        params.addValue("phone", "+380000000000");
        params.addValue("password", "password");
        params.addValue("admin_number", "12345678");

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO \"user\"(first_name, last_name, email, phone, password, admin_number) "
                    + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null phone value")
    void failed_to_insert_null_phone_value() {
        var params = new MapSqlParameterSource();
        params.addValue("first_name", "John");
        params.addValue("last_name", "Smith");
        params.addValue("email", "test@email.com");
        params.addValue("phone", null);
        params.addValue("password", "password");
        params.addValue("admin_number", "12345678");

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO \"user\"(first_name, last_name, email, phone, password, admin_number) "
                    + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null password value")
    void failed_to_insert_null_password_value() {
        var params = new MapSqlParameterSource();
        params.addValue("first_name", "John");
        params.addValue("last_name", "Smith");
        params.addValue("email", "test@email.com");
        params.addValue("phone", "+380000000000");
        params.addValue("password", null);
        params.addValue("admin_number", "12345678");

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO \"user\"(first_name, last_name, email, phone, password, admin_number) "
                    + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
