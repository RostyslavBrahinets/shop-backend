package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.ProductsCartsDao;
import com.shop.repositories.ProductsCartsRepository;
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
        ProductsCartsRepository.class,
        ProductsCartsRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductsCartsRepositoryContextConfigurationTest {
    @Autowired
    private ProductsCartsDao productsCartsDao;
    @Autowired
    private ProductsCartsRepository productsCartsRepository;

    @Test
    @DisplayName("Get all products in cart")
    void get_all_products_in_cart() {
        productsCartsRepository.findAllProductsInCart(1);

        verify(productsCartsDao).findAllProductsInCart(1);
    }

    @Test
    @DisplayName("Save product to cart")
    void save_product_to_cart() {
        productsCartsRepository.saveProductToCart(1, 1);

        verify(productsCartsDao).saveProductToCart(1, 1);
    }

    @Test
    @DisplayName("Delete product from cart")
    void delete_product_from_cart() {
        productsCartsRepository.deleteProductFromCart(1, 1);

        verify(productsCartsDao).deleteProductFromCart(1, 1);
    }

    @Test
    @DisplayName("Delete products from cart")
    void delete_products_from_cart() {
        productsCartsRepository.deleteProductsFromCart(1);

        verify(productsCartsDao).deleteProductsFromCart(1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public ProductsCartsDao productsCartsDao() {
            return mock(ProductsCartsDao.class);
        }
    }
}
