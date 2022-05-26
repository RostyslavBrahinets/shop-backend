package com.shop.dao;

import com.shop.configs.DatabaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/products_baskets/V20220421162626__Create_table_products_baskets.sql"
})
public class ProductsBasketsDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductsBasketsDao productsBasketsDao;

    @BeforeEach
    void setUp() {
        productsBasketsDao = new ProductsBasketsDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "products_baskets"
        );
    }

    private int fetchProductsBasketsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "products_baskets"
        );
    }

    @Test
    @DisplayName("Save product to basket")
    void save_product_to_basket() {
        productsBasketsDao.saveProductToBasket(1, 1);

        var productsBasketsCount = fetchProductsBasketsCount();

        assertThat(productsBasketsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple product to basket")
    void save_multiple_product_to_basket() {
        productsBasketsDao.saveProductToBasket(1, 1);
        productsBasketsDao.saveProductToBasket(2, 1);

        var productsBasketsCount = fetchProductsBasketsCount();

        assertThat(productsBasketsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Products from basket not deleted in case when not exists")
    void products_from_basket_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> productsBasketsDao
            .deleteProductsFromBasket(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Products from basket deleted")
    void products_from_basket_deleted() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("product_id", 1),
                Map.entry("basket_id", 1)
            ),
            Map.ofEntries(
                Map.entry("product_id", 2),
                Map.entry("basket_id", 1)
            ));

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("products_baskets")
            .usingColumns("product_id", "basket_id")
            .executeBatch(batchInsertParameters);

        var productsBasketsCountBeforeDeletion = fetchProductsBasketsCount();

        assertThat(productsBasketsCountBeforeDeletion).isEqualTo(2);

        productsBasketsDao.deleteProductsFromBasket(1);

        var productsBasketsCount = fetchProductsBasketsCount();

        assertThat(productsBasketsCount).isZero();
    }
}
