package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.ProductCategoryDao;
import com.shop.dao.ProductsBasketsDao;
import com.shop.repositories.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ProductCategoryRepository.class,
        ProductCategoryRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductCategoryRepositoryContextConfigurationTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    @DisplayName("Get all products in category")
    void get_all_products_in_category() {
        productCategoryRepository.findAllProductsInCategory(1);

        verify(productCategoryDao).findAllProductsInCategory(1);
    }

    @Test
    @DisplayName("Get category for product")
    void get_category_for_product() {
        productCategoryRepository.findCategoryForProduct(1);

        verify(productCategoryDao).findCategoryForProduct(1);
    }

    @Test
    @DisplayName("Save product to category")
    void save_product_to_category() {
        productCategoryRepository.saveProductToCategory(1, 1);

        verify(productCategoryDao).saveProductToCategory(1, 1);
    }

    @Test
    @DisplayName("Delete product from category")
    void delete_product_from_category() {
        productCategoryRepository.deleteProductFromCategory(1, 1);

        verify(productCategoryDao).deleteProductFromCategory(1, 1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public ProductCategoryDao productCategoryDao() {
            return mock(ProductCategoryDao.class);
        }
    }
}
