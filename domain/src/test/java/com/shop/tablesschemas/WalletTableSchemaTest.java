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
    "classpath:db/migration/wallet/V20220421162043__Create_table_wallet.sql"
})
public class WalletTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "wallet"
        );
    }

    @Test
    @DisplayName("Failed to insert null number value")
    void failed_to_insert_null_number_value() {
        var params = new MapSqlParameterSource();
        params.addValue("number", null);
        params.addValue("amount_of_money", 0);
        params.addValue("person_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO wallet(number, amount_of_money, person_id) "
                    + "VALUES (:number, :amount_of_money, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null amount of money value")
    void failed_to_insert_null_amount_of_money_value() {
        var params = new MapSqlParameterSource();
        params.addValue("number", "123");
        params.addValue("amount_of_money", null);
        params.addValue("person_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO wallet(number, amount_of_money, person_id) "
                    + "VALUES (:number, :amount_of_money, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null person id value")
    void failed_to_insert_null_person_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("number", "123");
        params.addValue("amount_of_money", 0);
        params.addValue("person_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO wallet(number, amount_of_money, person_id) "
                    + "VALUES (:number, :amount_of_money, :person_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
