package com.shop.integration.services.contextconfiguration;

import com.shop.repositories.ProductsBasketsRepository;
import com.shop.services.BasketService;
import com.shop.services.ProductService;
import com.shop.services.ProductsBasketsService;
import com.shop.services.WalletService;
import com.shop.stripe.StripePayment;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.ProductValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ProductsBasketsService.class,
        ProductsBasketsServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductsBasketsServiceContextConfigurationTest {
    @Autowired
    private ProductsBasketsRepository productsBasketsRepository;
    @Autowired
    private BasketValidator basketValidator;
    @Autowired
    private ProductsBasketsService productsBasketsService;

    @Test
    @DisplayName("Get all products in baskets")
    void get_all_products_in_baskets() {
        long basketId = 1;

        productsBasketsService.findAllProductsInBasket(basketId);

        verify(basketValidator, atLeast(1)).validate(basketId);
        verify(productsBasketsRepository).findAllProductsInBasket(basketId);
    }

    @Test
    @DisplayName("Delete all products from basket")
    void delete_all_products_from_basket() {
        long basketId = 1;

        productsBasketsService.deleteProductsFromBasket(basketId);

        verify(basketValidator, atLeast(1)).validate(basketId);
        verify(productsBasketsRepository).deleteProductsFromBasket(basketId);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public ProductsBasketsRepository productsBasketsRepository() {
            return mock(ProductsBasketsRepository.class);
        }

        @Bean
        public BasketService basketService() {
            return mock(BasketService.class);
        }

        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }

        @Bean
        public WalletService walletService() {
            return mock(WalletService.class);
        }

        @Bean
        public ProductValidator productValidator() {
            return mock(ProductValidator.class);
        }

        @Bean
        public BasketValidator basketValidator() {
            return mock(BasketValidator.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }

        @Bean
        public StripePayment stripePayment() {
            return mock(StripePayment.class);
        }
    }
}
