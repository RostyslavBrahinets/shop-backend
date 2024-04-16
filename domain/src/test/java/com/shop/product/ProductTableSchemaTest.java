package com.shop.product;

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

import static com.shop.SqlMigrationClasspath.PRODUCT;
import static com.shop.product.ProductParameter.*;
import static org.assertj.core.api.Assertions.assertThatCode;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    PRODUCT
})
class ProductTableSchemaTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "product"
        );
    }

    @Test
    @DisplayName("Failed to insert null name value")
    void failed_to_insert_null_name_value() {
        var params = new MapSqlParameterSource();
        setParams(params, null, getDescribe(), getPrice(), getBarcode(), isInStock());

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO product(name, describe, price, barcode, in_stock) "
                    + "VALUES (:name, :describe, :price, :barcode, :in_stock)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null describe value")
    void failed_to_insert_null_describe_value() {
        var params = new MapSqlParameterSource();
        setParams(params, getName(), null, getPrice(), getBarcode(), isInStock());

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO product(name, describe, price, barcode, in_stock) "
                    + "VALUES (:name, :describe, :price, :barcode, :in_stock)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null price value")
    void failed_to_insert_null_price_value() {
        var params = new MapSqlParameterSource();
        setParams(params, getName(), getDescribe(), null, getBarcode(), isInStock());

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO product(name, describe, price, barcode, in_stock) "
                    + "VALUES (:name, :describe, :price, :barcode, :in_stock)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null barcode value")
    void failed_to_insert_null_barcode_value() {
        var params = new MapSqlParameterSource();
        setParams(params, getName(), getDescribe(), getPrice(), null, isInStock());

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO product(name, describe, price, barcode, in_stock) "
                    + "VALUES (:name, :describe, :price, :barcode, :in_stock)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Failed to insert null in stock value")
    void failed_to_insert_null_in_stock_value() {
        var params = new MapSqlParameterSource();
        setParams(params, getName(), getDescribe(), getPrice(), getBarcode(), null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO product(name, describe, price, barcode, in_stock) "
                    + "VALUES (:name, :describe, :price, :barcode, :in_stock)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    private static void setParams(
        MapSqlParameterSource params,
        String name,
        String describe,
        Double price,
        String barcode,
        Boolean inStock
    ) {
        params.addValue("name", name);
        params.addValue("describe", describe);
        params.addValue("price", price);
        params.addValue("barcode", barcode);
        params.addValue("in_stock", inStock);
    }
}
