package com.shop.tables_schemas;

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
    "classpath:db/migration/products_baskets/V20220421162626__Create_table_products_baskets.sql"
})
public class ProductsBasketsTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "products_baskets"
        );
    }

    @Test
    @DisplayName("Failed to insert null product id value")
    void failed_to_insert_null_product_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("product_id", null);
        params.addValue("basket_id", 1);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO products_baskets(product_id, basket_id) "
                    + "VALUES (:product_id, :basket_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null basket id value")
    void failed_to_insert_null_basket_id_value() {
        var params = new MapSqlParameterSource();
        params.addValue("product_id", 1);
        params.addValue("basket_id", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO products_baskets(product_id, basket_id) "
                    + "VALUES (:product_id, :basket_id)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
