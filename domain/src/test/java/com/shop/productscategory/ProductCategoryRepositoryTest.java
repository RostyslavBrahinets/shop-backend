package com.shop.productscategory;

import com.shop.category.Category;
import com.shop.category.CategoryParameter;
import com.shop.configs.DatabaseConfig;
import com.shop.product.Product;
import com.shop.product.ProductParameter;
import com.shop.productcategory.ProductCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.shop.SqlMigrationClasspath.*;
import static com.shop.category.CategoryParameter.getCategoryWithId;
import static com.shop.product.ProductParameter.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    PRODUCT_CATEGORY,
    PRODUCT,
    CATEGORY
})
class ProductCategoryRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductCategoryRepository productCategoryRepository;

    @BeforeEach
    void setUp() {
        productCategoryRepository = new ProductCategoryRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "product_category",
            "product",
            "category"
        );
    }

    private int fetchProductCategoryCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "product_category"
        );
    }

    @Test
    @DisplayName("All products from category was found")
    void all_products_from_category_was_found() {
        insertTestDataToDb();

        List<Product> products = productCategoryRepository.findAllProductsInCategory(1);

        assertThat(products).isEqualTo(
            List.of(getProductWithId())
        );
    }

    @Test
    @DisplayName("Category for product was found")
    void category_for_product_was_found() {
        insertTestDataToDb();

        Optional<Category> category = productCategoryRepository.findCategoryForProduct(getProductId());

        assertThat(category).get().isEqualTo(getCategoryWithId());
    }

    @Test
    @DisplayName("Save product to category")
    void save_product_to_category() {
        productCategoryRepository.saveProductToCategory(getProductId(), getCategoryId());

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple products to category")
    void save_multiple_products_to_category() {
        productCategoryRepository.saveProductToCategory(getProductId(), getCategoryId());
        productCategoryRepository.saveProductToCategory(getProductId2(), getCategoryId());

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Delete product from category")
    void delete_product_from_category() {
        productCategoryRepository.saveProductToCategory(getProductId(), getCategoryId());

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(1);

        productCategoryRepository.deleteProductFromCategory(getProductId(), getCategoryId());

        productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isZero();
    }

    private void insertTestDataToDb() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name", "describe", "price", "barcode", "in_stock", "image")
            .execute(ProductParameter.getMapOfEntries(getBarcode()));

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(CategoryParameter.getMapOfEntries(CategoryParameter.getName()));

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product_category")
            .usingColumns("product_id", "category_id")
            .execute(Map.ofEntries(
                Map.entry("product_id", 1),
                Map.entry("category_id", 1)
            ));
    }
}
