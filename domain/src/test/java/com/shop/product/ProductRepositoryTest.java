package com.shop.product;

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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.shop.SqlMigrationClasspath.PRODUCT;
import static com.shop.product.ProductParameter.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    PRODUCT
})
class ProductRepositoryTest {
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
    @DisplayName("Find all products")
    void find_all_products() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            getMapOfEntries(getBarcode()),
            getMapOfEntries(getBarcode2())
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .executeBatch(batchInsertParameters);

        List<Product> products = productRepository.findAll();

        assertThat(products).isEqualTo(
            List.of(
                getProductWithId(),
                getProductWithId(getProductId2(), getBarcode2())
            )
        );
    }

    @Test
    @DisplayName("Product by id was not found")
    void product_by_id_was_not_found() {
        Optional<Product> product = productRepository.findById(getProductId());

        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("Product by id was found")
    void product_by_id_was_found() {
        insertTestDataToDb();

        Optional<Product> product = productRepository.findById(getProductId());

        assertThat(product).get().isEqualTo(getProductWithId());
    }

    @Test
    @DisplayName("Save product")
    void save_product() {
        productRepository.save(getProductWithoutId());

        var productsCount = fetchProductsCount();

        assertThat(productsCount).isEqualTo(getProductId());
    }

    @Test
    @DisplayName("Save multiple products")
    void save_multiple_products() {
        productRepository.save(getProductWithoutId());
        productRepository.save(getProductWithoutId(getBarcode2()));

        var productsCount = fetchProductsCount();

        assertThat(productsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Update product")
    void update_product() {
        insertTestDataToDb();

        Optional<Product> product = productRepository.update(
            getProductId(),
            getProductWithoutId2()
        );

        assertThat(product).get().isEqualTo(
            getProductWithId2()
        );
    }

    @Test
    @DisplayName("Product not deleted in case when not exists")
    void product_not_deleted_in_case_when_not_exists() {
        assertThatCode(
            () -> productRepository.delete(
                Product.of(getBarcode())
            )
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete product")
    void delete_product() {
        insertTestDataToDb();

        var productsCountBeforeDeletion = fetchProductsCount();

        assertThat(productsCountBeforeDeletion).isEqualTo(1);

        productRepository.delete(Product.of(getBarcode()));

        var productsCount = fetchProductsCount();

        assertThat(productsCount).isZero();
    }

    @Test
    @DisplayName("Product by barcode was not found")
    void product_by_barcode_was_not_found() {
        Optional<Product> product = productRepository.findByBarcode(getBarcode());

        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("Product by barcode was found")
    void product_by_barcode_was_found() {
        insertTestDataToDb();

        Optional<Product> product = productRepository.findByBarcode(getBarcode());

        assertThat(product).get().isEqualTo(getProductWithId());
    }

    @Test
    @DisplayName("Save image for product")
    void save_image_for_product() {
        insertTestDataToDb();

        productRepository.saveImageForProduct(getImage2(), getProductId());

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT image FROM product WHERE id=:id",
            Map.ofEntries(Map.entry("id", getProductId())),
            byte[].class
        );

        assertThat(updatedBasket).isEqualTo(getImage2());
    }

    private void insertTestDataToDb() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .execute(getMapOfEntries(getBarcode()));
    }

    private static Map<String, Serializable> getMapOfEntries(String barcode) {
        return Map.ofEntries(
            Map.entry("name", getName()),
            Map.entry("describe", getDescribe()),
            Map.entry("price", getPrice()),
            Map.entry("barcode", barcode),
            Map.entry("in_stock", isInStock()),
            Map.entry("image", getImage())
        );
    }
}
