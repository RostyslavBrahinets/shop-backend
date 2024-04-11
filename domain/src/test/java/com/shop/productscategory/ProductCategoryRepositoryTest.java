package com.shop.productscategory;

import com.shop.category.Category;
import com.shop.configs.DatabaseConfig;
import com.shop.product.Product;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/productcategory/V20220421162206__Create_table_product_category.sql",
    "classpath:db/migration/product/V20220421162205__Create_table_product.sql",
    "classpath:db/migration/category/V20220421162204__Create_table_category.sql"
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

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(Map.ofEntries(Map.entry("name", "name")));

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product_category")
            .usingColumns("product_id", "category_id")
            .execute(Map.ofEntries(
                Map.entry("product_id", 1),
                Map.entry("category_id", 1)
            ));

        List<Product> products = productCategoryRepository.findAllProductsInCategory(1);

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
                    .withId(1)
            )
        );
    }

    @Test
    @DisplayName("Category for product was found")
    void category_for_product_was_found() {
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

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("category")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(Map.ofEntries(Map.entry("name", "name")));

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("product_category")
            .usingColumns("product_id", "category_id")
            .execute(Map.ofEntries(
                Map.entry("product_id", 1),
                Map.entry("category_id", 1)
            ));

        Optional<Category> category = productCategoryRepository.findCategoryForProduct(1);

        assertThat(category).get().isEqualTo(
            Category.of("name").withId(1)
        );
    }

    @Test
    @DisplayName("Save product to category")
    void save_product_to_category() {
        productCategoryRepository.saveProductToCategory(1, 1);

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple products to category")
    void save_multiple_products_to_category() {
        productCategoryRepository.saveProductToCategory(1, 1);
        productCategoryRepository.saveProductToCategory(2, 1);

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Delete product from category")
    void delete_product_from_category() {
        productCategoryRepository.saveProductToCategory(1, 1);

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(1);

        productCategoryRepository.deleteProductFromCategory(1, 1);

        productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(0);
    }
}
