package com.shop.integration.services.contextconfiguration;

import com.shop.models.Cart;
import com.shop.models.User;
import com.shop.repositories.CartRepository;
import com.shop.services.CartService;
import com.shop.services.UserService;
import com.shop.validators.CartValidator;
import com.shop.validators.UserValidator;
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
        CartService.class,
        CartServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class CartServiceContextConfigurationTest {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartValidator cartValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("Get all carts")
    void get_all_carts() {
        cartService.findAll();

        verify(cartRepository).findAll();
    }

    @Test
    @DisplayName("Get cart by id")
    void get_cart_by_id() {
        long id = 1;

        cartService.findById(id);

        verify(cartValidator, atLeast(1)).validate(id);
        verify(cartRepository).findById(id);
    }

    @Test
    @DisplayName("Get cart by user")
    void get_cart_by_user() {
        long userId = 1;
        List<User> users = userService.findAll();

        cartService.findByUser(userId);

        verify(userValidator, atLeast(1)).validate(userId, users);
        verify(cartRepository).findByUser(userId);
    }

    @Test
    @DisplayName("Save cart")
    void save_cart() {
        long userId = 1;
        double totalCost = 0;
        List<User> users = userService.findAll();

        cartService.save(totalCost, userId);

        verify(cartValidator, atLeast(1)).validate(totalCost);
        verify(userValidator, atLeast(1)).validate(userId, users);
        verify(cartRepository).save(totalCost, userId);
    }

    @Test
    @DisplayName("Update cart")
    void update_cart() {
        long id = 1;
        double totalCost = 100;

        cartService.update(id, totalCost);

        verify(cartValidator, atLeast(1)).validate(id);
        verify(cartValidator, atLeast(1)).validate(totalCost);
        verify(cartRepository).update(1, totalCost);
    }

    @Test
    @DisplayName("Delete cart")
    void delete_cart() {
        long id = 1;

        cartService.delete(id);

        verify(cartValidator, atLeast(1)).validate(id);
        verify(cartRepository).delete(id);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Cart cart() {
            return mock(Cart.class);
        }

        @Bean
        public CartRepository cartRepository() {
            return mock(CartRepository.class);
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
    }
}
