package com.shop.dao;

import com.shop.configs.DatabaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/product_category/V20220421162206__Create_table_product_category.sql",
})
public class ProductCategoryDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductCategoryDao productCategoryDao;

    @BeforeEach
    void setUp() {
        productCategoryDao = new ProductCategoryDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "product_category"
        );
    }

    private int fetchProductCategoryCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "product_category"
        );
    }

    @Test
    @DisplayName("Save product to category")
    void save_product_to_category() {
        productCategoryDao.saveProductToCategory(1, 1);

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple products to category")
    void save_multiple_products_to_category() {
        productCategoryDao.saveProductToCategory(1, 1);
        productCategoryDao.saveProductToCategory(2, 1);

        var productCategoryCount = fetchProductCategoryCount();

        assertThat(productCategoryCount).isEqualTo(2);
    }
}
