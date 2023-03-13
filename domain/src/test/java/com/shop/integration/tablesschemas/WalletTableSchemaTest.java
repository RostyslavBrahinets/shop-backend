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
    "classpath:db/migration/admin_number/V20220421160504__Create_table_admin_number.sql",
    "classpath:db/migration/user/V20220421161642__Create_table_user.sql",
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
        params.addValue("user_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO wallet(number, amount_of_money, user_id) "
                    + "VALUES (:number, :amount_of_money, :user_id)",
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
        params.addValue("user_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO wallet(number, amount_of_money, user_id) "
                    + "VALUES (:number, :amount_of_money, :user_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null user id value")
    void failed_to_insert_null_user_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("number", "123");
        params.addValue("amount_of_money", 0);
        params.addValue("user_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO wallet(number, amount_of_money, user_id) "
                    + "VALUES (:number, :amount_of_money, :user_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
