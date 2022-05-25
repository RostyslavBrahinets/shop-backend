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
    "classpath:db/migration/contact/V20220421161842__Create_table_contact.sql"
})
public class ContactTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "contact"
        );
    }

    @Test
    @DisplayName("Failed to insert null email value")
    void failed_to_insert_null_email_value() {
        var params = new MapSqlParameterSource();
        params.addValue("email", null);
        params.addValue("phone", "+380000000000");
        params.addValue("password", "password");
        params.addValue("person_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO contact(email, phone, password, person_id) "
                    + "VALUES (:email, :phone, :password, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null phone value")
    void failed_to_insert_null_phone_value() {
        var params = new MapSqlParameterSource();
        params.addValue("email", "test@email.com");
        params.addValue("phone", null);
        params.addValue("password", "password");
        params.addValue("person_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO contact(email, phone, password, person_id) "
                    + "VALUES (:email, :phone, :password, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null password value")
    void failed_to_insert_null_password_value() {
        var params = new MapSqlParameterSource();
        params.addValue("email", "test@email.com");
        params.addValue("phone", "+380000000000");
        params.addValue("password", null);
        params.addValue("person_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO contact(email, phone, password, person_id) "
                    + "VALUES (:email, :phone, :password, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null person id value")
    void failed_to_insert_null_person_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("email", "test@email.com");
        params.addValue("phone", "+380000000000");
        params.addValue("password", "password");
        params.addValue("person_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO contact(email, phone, password, person_id) "
                    + "VALUES (:email, :phone, :password, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
