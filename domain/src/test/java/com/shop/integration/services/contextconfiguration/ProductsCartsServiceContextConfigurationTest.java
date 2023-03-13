package com.shop.integration.services.contextconfiguration;

import com.shop.models.Cart;
import com.shop.models.Product;
import com.shop.models.User;
import com.shop.models.Wallet;
import com.shop.repositories.ProductsCartsRepository;
import com.shop.services.*;
import com.shop.stripe.StripePayment;
import com.shop.validators.CartValidator;
import com.shop.validators.ProductValidator;
import com.shop.validators.UserValidator;
import com.shop.validators.WalletValidator;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ProductsCartsService.class,
        ProductsCartsServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductsCartsServiceContextConfigurationTest {
    @Autowired
    private ProductsCartsRepository productsCartsRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private CartValidator cartValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private WalletValidator walletValidator;
    @Autowired
    private StripePayment stripePayment;
    @Autowired
    private ProductsCartsService productsCartsService;

    private List<User> users;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        users = List.of();
        products = List.of();
    }

    @Test
    @DisplayName("Get all products in carts")
    void get_all_products_in_carts() {
        long cartId = 1;

        productsCartsService.findAllProductsInCart(cartId);

        verify(cartValidator, atLeast(1)).validate(cartId);
        verify(productsCartsRepository).findAllProductsInCart(cartId);
    }

    @Test
    @DisplayName("Save product to cart")
    void save_product_to_cart() {
        long productId = 1;
        long cartId = 1;

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

        when(cartService.findById(cartId))
            .thenReturn(Cart.of(0).withId(1));

        cartService.update(cartId, 100);

        productsCartsService.saveProductToCart(productId, cartId);

        verify(productValidator, atLeast(1)).validate(productId, products);
        verify(cartValidator, atLeast(1)).validate(cartId);
        verify(productsCartsRepository).saveProductToCart(productId, cartId);
    }

    @Test
    @DisplayName("Delete product to cart")
    void delete_product_from_cart() {
        long productId = 1;
        long cartId = 1;

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

        when(cartService.findById(cartId))
            .thenReturn(Cart.of(0).withId(1));

        cartService.update(cartId, 100);

        productsCartsService.deleteProductFromCart(productId, cartId);

        verify(productValidator, atLeast(1)).validate(productId, products);
        verify(cartValidator, atLeast(1)).validate(cartId);
        verify(productsCartsRepository).deleteProductFromCart(productId, cartId);
    }

    @Test
    @DisplayName("Delete all products from cart")
    void delete_all_products_from_cart() {
        long cartId = 1;

        productsCartsService.deleteProductsFromCart(cartId);

        verify(cartValidator, atLeast(1)).validate(cartId);
        verify(productsCartsRepository, atLeast(1)).deleteProductsFromCart(cartId);
    }

    @Test
    @DisplayName("Buy products in cart")
    void buy_products_in_cart() throws StripeException {
        long userId = 1;

        when(walletService.findByUser(userId))
            .thenReturn(Wallet.of("123", 100).withId(1));

        when(cartService.findByUser(userId))
            .thenReturn(Cart.of(0).withId(1));

        walletService.update(1, 100);
        stripePayment.updateCustomer("123", 100);
        cartService.update(1, 0);

        productsCartsService.buy(userId);

        verify(userValidator, atLeast(1)).validate(userId, users);
        verify(walletValidator, atLeast(1)).validateAmountOfMoney(100);
        verify(cartValidator, atLeast(1)).validate(1);
        verify(productsCartsRepository, atLeast(1)).deleteProductsFromCart(1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public ProductsCartsRepository productsCartsRepository() {
            return mock(ProductsCartsRepository.class);
        }

        @Bean
        public CartService cartService() {
            return mock(CartService.class);
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
        public CartValidator cartValidator() {
            return mock(CartValidator.class);
        }

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public UserValidator userValidator() {
            return mock(UserValidator.class);
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
