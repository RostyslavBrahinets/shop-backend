package com.shop.services.basket;

import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
import com.shop.services.BasketService;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
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
        BasketService.class,
        BasketServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class BasketServiceContextConfigurationTest {
    @Autowired
    private Basket basket;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private BasketValidator basketValidator;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private BasketService basketService;

    @Test
    @DisplayName("Get all baskets")
    void get_all_baskets() {
        basketService.findAll();

        verify(basketRepository).findAll();
    }

    @Test
    @DisplayName("Save basket")
    void save_basket() {
        basketService.save(basket, 1);

        verify(basketValidator, atLeast(1)).validate(basket);
        verify(personValidator, atLeast(1)).validate(1);
        verify(basketRepository).save(basket, 1);
    }

    @Test
    @DisplayName("Update basket")
    void update_basket() {
        basketService.update(1, basket);

        verify(basketValidator, atLeast(1)).validate(1);
        verify(basketValidator, atLeast(1)).validate(basket);
        verify(basketRepository).update(1, basket);
    }

    @Test
    @DisplayName("Delete basket")
    void delete_basket() {
        basketService.delete(1);

        verify(basketValidator, atLeast(1)).validate(1);
        verify(basketRepository).delete(1);
    }

    @Test
    @DisplayName("Get basket by id")
    void get_basket_by_id() {
        basketService.findById(1);

        verify(basketValidator, atLeast(1)).validate(1);
        verify(basketRepository).findById(1);
    }

    @Test
    @DisplayName("Get basket by person")
    void get_basket_by_person() {
        basketService.findByPerson(1);

        verify(personValidator, atLeast(1)).validate(1);
        verify(basketRepository).findByPerson(1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Basket basket() {
            return mock(Basket.class);
        }

        @Bean
        public BasketRepository basketRepository() {
            return mock(BasketRepository.class);
        }

        @Bean
        public BasketValidator basketValidator() {
            return mock(BasketValidator.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }
    }
}
