package com.shop.integration.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.repositories.ProductsCartsRepository;
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
    "classpath:db/migration/products_carts/V20220421162626__Create_table_products_carts.sql",
    "classpath:db/migration/product/V20220421162205__Create_table_product.sql"
})
public class ProductsCartsRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductsCartsRepository productsCartsRepository;

    @BeforeEach
    void setUp() {
        productsCartsRepository = new ProductsCartsRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "products_carts", "product"
        );
    }

    private int fetchProductsCartsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "products_carts"
        );
    }

    @Test
    @DisplayName("Get all products from cart")
    void get_all_products_from_cart() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("products_carts")
            .usingColumns("product_id", "cart_id")
            .execute(
                Map.ofEntries(
                    Map.entry("product_id", 1),
                    Map.entry("cart_id", 1)
                )
            );

        productsCartsRepository.findAllProductsInCart(1);

        var productsCartsCount = fetchProductsCartsCount();

        assertThat(productsCartsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save product to cart")
    void save_product_to_cart() {
        productsCartsRepository.saveProductToCart(1, 1);

        var productsCartsCount = fetchProductsCartsCount();

        assertThat(productsCartsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple product to cart")
    void save_multiple_product_to_cart() {
        productsCartsRepository.saveProductToCart(1, 1);
        productsCartsRepository.saveProductToCart(2, 1);

        var productsCartsCount = fetchProductsCartsCount();

        assertThat(productsCartsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Products from cart not deleted in case when not exists")
    void products_from_cart_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> productsCartsRepository
            .deleteProductsFromCart(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Products from cart deleted")
    void products_from_cart_deleted() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("product_id", 1),
                Map.entry("cart_id", 1)
            ),
            Map.ofEntries(
                Map.entry("product_id", 2),
                Map.entry("cart_id", 1)
            ));

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("products_carts")
            .usingColumns("product_id", "cart_id")
            .executeBatch(batchInsertParameters);

        var productsCartsCountBeforeDeletion = fetchProductsCartsCount();

        assertThat(productsCartsCountBeforeDeletion).isEqualTo(2);

        productsCartsRepository.deleteProductsFromCart(1);

        var productsCartsCount = fetchProductsCartsCount();

        assertThat(productsCartsCount).isZero();
    }
}
