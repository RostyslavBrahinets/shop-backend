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

import static com.shop.SqlMigrationClasspath.ADMIN_NUMBER;
import static com.shop.SqlMigrationClasspath.USER;
import static com.shop.adminnumber.AdminNumberParameter.getNumber;
import static com.shop.user.UserParameter.*;
import static org.assertj.core.api.Assertions.assertThatCode;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    ADMIN_NUMBER,
    USER
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
        setParams(params, null, getLastName(), getEmail(), getPhone(), getPassword(), getNumber());

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
        setParams(params, getFirstName(), null, getEmail(), getPhone(), getPassword(), getNumber());

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
        setParams(params, getFirstName(), getLastName(), null, getPhone(), getPassword(), getNumber());

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
        setParams(params, getFirstName(), getLastName(), getEmail(), null, getPassword(), getNumber());

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
        setParams(params, getFirstName(), getLastName(), getEmail(), getPhone(), null, getNumber());

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO \"user\"(first_name, last_name, email, phone, password, admin_number) "
                    + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    private static void setParams(
        MapSqlParameterSource params,
        String firstName,
        String lastName,
        String email,
        String phone,
        char[] password,
        String adminNumber
    ) {
        params.addValue("first_name", firstName);
        params.addValue("last_name", lastName);
        params.addValue("email", email);
        params.addValue("phone", phone);
        params.addValue("password", password != null ? String.valueOf(password) : null);
        params.addValue("admin_number", adminNumber);
    }
}
