package com.shop.cart;

import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.shop.cart.CartParameter.*;
import static com.shop.user.UserParameter.getUserId;
import static com.shop.user.UserParameter.getUsers;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        CartService.class,
        CartServiceContextConfigurationTest.TestContextConfig.class
    }
)
class CartServiceContextConfigurationTest {
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

        verify(cartRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get cart by id")
    void get_cart_by_id() {
        cartService.findById(getCartId());

        verify(cartValidator, atLeast(1)).validate(getCartId());
        verify(cartRepository).findById(getCartId());
    }

    @Test
    @DisplayName("Get cart by user")
    void get_cart_by_user() {
        cartService.findByUser(User.of(null, null).withId(getUserId()));

        verify(userValidator, atLeast(1)).validate(getUserId(), getUsers());
        verify(cartRepository).findByUser(User.of(null, null).withId(getUserId()));
    }

    @Test
    @DisplayName("Save cart")
    void save_cart() {
        cartService.save(getCartWithoutId());

        verify(cartValidator, atLeast(1)).validate(getPriceAmount());
        verify(userValidator, atLeast(1)).validate(getUserId(), getUsers());
        verify(cartRepository).save(getCartWithId());
    }

    @Test
    @DisplayName("Update cart")
    void update_cart() {
        cartService.update(getCartId(), getCartWithoutId(getPriceAmount2()));

        verify(cartValidator, atLeast(1)).validate(getCartId());
        verify(cartValidator, atLeast(1)).validate(getPriceAmount2());
        verify(cartRepository).update(getCartId(), getCartWithoutId(getPriceAmount2()));
    }

    @Test
    @DisplayName("Delete cart")
    void delete_cart() {
        cartService.delete(getCartWithId());

        verify(cartValidator, atLeast(1)).validate(getCartId());
        verify(cartRepository).delete(getCartWithId());
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
