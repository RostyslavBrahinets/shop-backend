package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.CartDao;
import com.shop.models.Cart;
import com.shop.repositories.CartRepository;
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
        CartRepository.class,
        CartRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class CartRepositoryContextConfigurationTest {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("Get all carts")
    void get_all_carts() {
        cartRepository.findAll();

        verify(cartDao, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get cart by id")
    void get_cart_by_id() {
        long id = 1;

        cartRepository.findById(id);

        verify(cartDao).findById(id);
    }

    @Test
    @DisplayName("Get cart by user")
    void get_cart_by_user() {
        long userId = 2;

        cartRepository.findByUser(userId);

        verify(cartDao).findByUser(userId);
    }

    @Test
    @DisplayName("Save cart")
    void save_cart() {
        long userId = 2;
        double totalCost = 0;

        cartRepository.save(totalCost, userId);

        verify(cartDao).save(totalCost, userId);
    }

    @Test
    @DisplayName("Update cart")
    void update_cart() {
        long id = 1;
        double totalCost = 100;

        cartRepository.update(id, totalCost);

        verify(cartDao).update(id, totalCost);
    }

    @Test
    @DisplayName("Delete cart")
    void delete_cart() {
        long id = 1;

        cartRepository.delete(id);

        verify(cartDao).delete(id);
    }

    @Test
    @DisplayName("Count carts")
    void count_carts() {
        cartRepository.count();

        verify(cartDao, atLeast(1)).findAll();
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Cart cart() {
            return mock(Cart.class);
        }

        @Bean
        public CartDao cartDao() {
            return mock(CartDao.class);
        }
    }
}
