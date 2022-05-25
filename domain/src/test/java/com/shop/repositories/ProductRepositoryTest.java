package com.shop.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.ProductDao;
import com.shop.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    ProductRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/product/V20220421162205__Create_table_product.sql"
})
public class ProductRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE product");
    }

    @Test
    @DisplayName("No products in database")
    void no_history_records_in_db() {
        int productsCount = productRepository.count();

        assertThat(productsCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing product")
    void nothing_happened_when_trying_to_delete_not_existing_product() {
        assertThatCode(() -> productRepository.delete("123"))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Product was deleted")
    @DirtiesContext
    void product_was_deleted() {
        var productToSave = Product.of(
            "name",
            "describe",
            100,
            "123",
            true,
            new byte[]{1, 1, 1}
        );

        productRepository.save(productToSave);

        assertThat(productRepository.count()).isEqualTo(1);

        productRepository.delete(productToSave.getBarcode());

        assertThat(productRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save product and check product data")
    @DirtiesContext
    void save_product_and_check_product_data() {
        var productToSave = Product.of(
            "name",
            "describe",
            100,
            "123",
            true,
            new byte[]{1, 1, 1}
        );
        productRepository.save(productToSave);
        var savedProduct = productRepository.findById(productRepository.count());
        Product product = null;
        if (savedProduct.isPresent()) {
            product = savedProduct.get();
        }

        assertThat(product).extracting(Product::getId).isEqualTo(1L);
        assertThat(product).extracting(Product::getName).isEqualTo("name");
        assertThat(product).extracting(Product::getDescribe).isEqualTo("describe");
        assertThat(product).extracting(Product::getPrice).isEqualTo(100.0);
        assertThat(product).extracting(Product::getBarcode).isEqualTo("123");
        assertThat(product).extracting(Product::isInStock).isEqualTo(true);
        assertThat(product).extracting(Product::getImage).isEqualTo(new byte[]{1, 1, 1});
    }

    @Test
    @DisplayName("Save multiple products")
    @DirtiesContext
    void save_multiple_products() {
        productRepository.save(
            Product.of(
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );
        productRepository.save(
            Product.of(
                "name",
                "describe",
                100,
                "456",
                true,
                new byte[]{1, 1, 1}
            )
        );

        assertThat(productRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Product was not found")
    void product_was_not_found() {
        Optional<Product> product = productRepository.findById(1);

        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("Product was found")
    @DirtiesContext
    void product_was_found() {
        productRepository.save(
            Product.of(
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );

        Optional<Product> product = productRepository.findById(productRepository.count());

        assertThat(product).get().isEqualTo(
            Product.of(
                    "name",
                    "describe",
                    100,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1));
    }

    @Test
    @DisplayName("Find all products")
    @DirtiesContext
    void find_all_products() {
        productRepository.save(
            Product.of(
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );
        productRepository.save(
            Product.of(
                "name",
                "describe",
                100,
                "456",
                true,
                new byte[]{1, 1, 1}
            )
        );

        var products = productRepository.findAll();

        assertThat(products).isEqualTo(
            List.of(
                Product.of(
                        "name",
                        "describe",
                        100,
                        "123",
                        true,
                        new byte[]{1, 1, 1}
                    )
                    .withId(1),
                Product.of(
                        "name",
                        "describe",
                        100,
                        "456",
                        true,
                        new byte[]{1, 1, 1}
                    )
                    .withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public ProductDao productDao() {
            return new ProductDao(namedParameterJdbcTemplate);
        }

        @Bean
        public ProductRepository basketRepository(ProductDao productDao) {
            return new ProductRepository(productDao);
        }
    }
}
