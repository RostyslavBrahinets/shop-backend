package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.ProductsBasketsDao;
import com.shop.repositories.ProductsBasketsRepository;
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
        ProductsBasketsRepository.class,
        ProductsBasketsRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductsBasketsRepositoryContextConfigurationTest {
    @Autowired
    private ProductsBasketsDao productsBasketsDao;
    @Autowired
    private ProductsBasketsRepository productsBasketsRepository;

    @Test
    @DisplayName("Get all products in basket")
    void get_all_products_in_basket() {
        productsBasketsRepository.findAllProductsInBasket(1);

        verify(productsBasketsDao).findAllProductsInBasket(1);
    }

    @Test
    @DisplayName("Save product to basket")
    void save_product_to_basket() {
        productsBasketsRepository.saveProductToBasket(1, 1);

        verify(productsBasketsDao).saveProductToBasket(1, 1);
    }

    @Test
    @DisplayName("Delete product from basket")
    void delete_product_from_basket() {
        productsBasketsRepository.deleteProductFromBasket(1, 1);

        verify(productsBasketsDao).deleteProductFromBasket(1, 1);
    }

    @Test
    @DisplayName("Delete products from basket")
    void delete_products_from_basket() {
        productsBasketsRepository.deleteProductsFromBasket(1);

        verify(productsBasketsDao).deleteProductsFromBasket(1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public ProductsBasketsDao productsBasketsDao() {
            return mock(ProductsBasketsDao.class);
        }
    }
}
