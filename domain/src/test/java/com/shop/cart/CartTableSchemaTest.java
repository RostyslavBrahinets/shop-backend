package com.shop.cart;

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

import static com.shop.SqlMigrationClasspath.*;
import static com.shop.cart.CartParameter.getPriceAmount;
import static com.shop.user.UserParameter.getUserId;
import static org.assertj.core.api.Assertions.assertThatCode;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    ADMIN_NUMBER,
    USER,
    CART
})
class CartTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "cart"
        );
    }

    @Test
    @DisplayName("Failed to insert null price amount value")
    void failed_to_insert_null_price_amount_value() {
        var params = new MapSqlParameterSource();
        params.addValue("price_amount", null);
        params.addValue("user_id", getUserId());

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO cart(price_amount, user_id) VALUES (:price_amount, :user_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null user id value")
    void failed_to_insert_null_user_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("price_amount", getPriceAmount());
        params.addValue("user_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO cart(price_amount, user_id) VALUES (:price_amount, :user_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
