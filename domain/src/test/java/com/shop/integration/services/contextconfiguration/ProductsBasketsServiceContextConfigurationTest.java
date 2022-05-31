package com.shop.integration.services.contextconfiguration;

import com.shop.models.Basket;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.repositories.ProductsBasketsRepository;
import com.shop.services.BasketService;
import com.shop.services.ProductService;
import com.shop.services.ProductsBasketsService;
import com.shop.services.WalletService;
import com.shop.stripe.StripePayment;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.ProductValidator;
import com.shop.validators.WalletValidator;
import com.stripe.exception.StripeException;
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
    private ProductService productService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private BasketValidator basketValidator;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private WalletValidator walletValidator;
    @Autowired
    private StripePayment stripePayment;
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
    @DisplayName("Save product to basket")
    void save_product_to_basket() {
        long productId = 1;
        long basketId = 1;

        when(productService.findById(productId))
            .thenReturn(Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1));

        when(basketService.findById(basketId))
            .thenReturn(Basket.of(0).withId(1));

        basketService.update(basketId, 100);

        productsBasketsService.saveProductToBasket(productId, basketId);

        verify(productValidator, atLeast(1)).validate(productId);
        verify(basketValidator, atLeast(1)).validate(basketId);
        verify(productsBasketsRepository).saveProductToBasket(productId, basketId);
    }

    @Test
    @DisplayName("Delete product to basket")
    void delete_product_from_basket() {
        long productId = 1;
        long basketId = 1;

        when(productService.findById(productId))
            .thenReturn(Product.of(
                    "name",
                    "describe",
                    0,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1));

        when(basketService.findById(basketId))
            .thenReturn(Basket.of(0).withId(1));

        basketService.update(basketId, 100);

        productsBasketsService.deleteProductFromBasket(productId, basketId);

        verify(productValidator, atLeast(1)).validate(productId);
        verify(basketValidator, atLeast(1)).validate(basketId);
        verify(productsBasketsRepository).deleteProductFromBasket(productId, basketId);
    }

    @Test
    @DisplayName("Delete all products from basket")
    void delete_all_products_from_basket() {
        long basketId = 1;

        productsBasketsService.deleteProductsFromBasket(basketId);

        verify(basketValidator, atLeast(1)).validate(basketId);
        verify(productsBasketsRepository, atLeast(1)).deleteProductsFromBasket(basketId);
    }

    @Test
    @DisplayName("Buy products in basket")
    void buy_products_in_basket() throws StripeException {
        long personId = 1;

        when(walletService.findByPerson(personId))
            .thenReturn(Wallet.of("123", 100).withId(1));

        when(basketService.findByPerson(personId))
            .thenReturn(Basket.of(0).withId(1));

        walletService.update(1, 100);
        stripePayment.updateCustomer("123", 100);
        basketService.update(1, 0);

        productsBasketsService.buy(personId);

        verify(personValidator, atLeast(1)).validate(personId);
        verify(walletValidator, atLeast(1)).validateAmountOfMoney(100);
        verify(basketValidator, atLeast(1)).validate(1);
        verify(productsBasketsRepository, atLeast(1)).deleteProductsFromBasket(1);
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
        public WalletValidator walletValidator() {
            return mock(WalletValidator.class);
        }

        @Bean
        public StripePayment stripePayment() {
            return mock(StripePayment.class);
        }
    }
}
