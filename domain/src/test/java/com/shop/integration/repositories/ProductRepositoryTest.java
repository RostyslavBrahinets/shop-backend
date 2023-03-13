package com.shop.integration.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.repositories.ProductRepository;
import com.shop.models.Product;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/product/V20220421162205__Create_table_product.sql"
})
public class ProductRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "product"
        );
    }

    private int fetchProductsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "product"
        );
    }

    @Test
    @DisplayName("Save product")
    void save_product() {
        productRepository.save(
            "name",
            "describe",
            0,
            "123",
            true,
            new byte[]{1, 1, 1}
        );

        var productsCount = fetchProductsCount();

        assertThat(productsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple products")
    void save_multiple_products() {
        productRepository.save(
            "name",
            "describe",
            0,
            "123",
            true,
            new byte[]{1, 1, 1}
        );
        productRepository.save(
            "name",
            "describe",
            0,
            "456",
            true,
            new byte[]{1, 1, 1}
        );

        var productsCount = fetchProductsCount();

        assertThat(productsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Product by id was not found")
    void product_by_id_was_not_found() {
        Optional<Product> product = productRepository.findById(1);

        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("Product by id was found")
    void product_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .execute(
                Map.ofEntries(
                    Map.entry("name", "name"),
                    Map.entry("describe", "describe"),
                    Map.entry("price", 0),
                    Map.entry("barcode", "123"),
                    Map.entry("in_stock", true),
                    Map.entry("image", new byte[]{1, 1, 1})
                )
            );

        Optional<Product> product = productRepository.findById(1);

        assertThat(product).get().isEqualTo(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1));
    }

    @Test
    @DisplayName("Product by barcode was not found")
    void product_by_barcode_was_not_found() {
        Optional<Product> product = productRepository.findByBarcode("123");

        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("Product by barcode was found")
    void product_by_barcode_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .execute(
                Map.ofEntries(
                    Map.entry("name", "name"),
                    Map.entry("describe", "describe"),
                    Map.entry("price", 0),
                    Map.entry("barcode", "123"),
                    Map.entry("in_stock", true),
                    Map.entry("image", new byte[]{1, 1, 1})
                )
            );

        Optional<Product> product = productRepository.findByBarcode("123");

        assertThat(product).get().isEqualTo(
            Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1));
    }

    @Test
    @DisplayName("Find all products")
    void find_all_products() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("name", "name"),
                Map.entry("describe", "describe"),
                Map.entry("price", 0),
                Map.entry("barcode", "123"),
                Map.entry("in_stock", true),
                Map.entry("image", new byte[]{1, 1, 1})
            ),
            Map.ofEntries(
                Map.entry("name", "name"),
                Map.entry("describe", "describe"),
                Map.entry("price", 0),
                Map.entry("barcode", "456"),
                Map.entry("in_stock", true),
                Map.entry("image", new byte[]{1, 1, 1})
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .executeBatch(batchInsertParameters);

        List<Product> products = productRepository.findAll();

        assertThat(products).isEqualTo(
            List.of(
                Product.of(
                        "name",
                        "describe",
                        0,
                        "123",
                        true,
                        new byte[]{1, 1, 1}
                    )
                    .withId(1),
                Product.of(
                        "name",
                        "describe",
                        0,
                        "456",
                        true,
                        new byte[]{1, 1, 1}
                    )
                    .withId(2)
            )
        );
    }

    @Test
    @DisplayName("Product not deleted in case when not exists")
    void product_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> productRepository.delete("123")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Product deleted")
    void product_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .execute(
                Map.ofEntries(
                    Map.entry("name", "name"),
                    Map.entry("describe", "describe"),
                    Map.entry("price", 0),
                    Map.entry("barcode", "123"),
                    Map.entry("in_stock", true),
                    Map.entry("image", new byte[]{1, 1, 1})
                )
            );

        var productsCountBeforeDeletion = fetchProductsCount();

        assertThat(productsCountBeforeDeletion).isEqualTo(1);

        productRepository.delete("123");

        var productsCount = fetchProductsCount();

        assertThat(productsCount).isZero();
    }

    @Test
    @DisplayName("Save image for product")
    void save_image_for_product() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .execute(
                Map.ofEntries(
                    Map.entry("name", "name"),
                    Map.entry("describe", "describe"),
                    Map.entry("price", 0),
                    Map.entry("barcode", "123"),
                    Map.entry("in_stock", true),
                    Map.entry("image", new byte[]{1, 1, 1})
                )
            );

        productRepository.saveImageForProduct(new byte[]{127, 127, 127}, 1);

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT image FROM product WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            byte[].class
        );

        assertThat(updatedBasket).isEqualTo(new byte[]{127, 127, 127});
    }
}
