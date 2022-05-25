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
    "classpath:db/migration/product/V20220421162205__Create_table_product.sql"
})
public class ProductTableSchemaTest {
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
        params.addValue("name", null);
        params.addValue("describe", "describe");
        params.addValue("price", 0);
        params.addValue("barcode", "123");
        params.addValue("in_stock", true);

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
        params.addValue("name", "name");
        params.addValue("describe", null);
        params.addValue("price", 0);
        params.addValue("barcode", "123");
        params.addValue("in_stock", true);

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
        params.addValue("name", "name");
        params.addValue("describe", "describe");
        params.addValue("price", null);
        params.addValue("barcode", "123");
        params.addValue("in_stock", true);

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
        params.addValue("name", "name");
        params.addValue("describe", "describe");
        params.addValue("price", 0);
        params.addValue("barcode", null);
        params.addValue("in_stock", true);

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
        params.addValue("name", "name");
        params.addValue("describe", "describe");
        params.addValue("price", 0);
        params.addValue("barcode", "123");
        params.addValue("in_stock", null);

        assertThatCode(
            () -> jdbcTemplate.update(
                "INSERT INTO product(name, describe, price, barcode, in_stock) "
                    + "VALUES (:name, :describe, :price, :barcode, :in_stock)",
                params
            )
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
