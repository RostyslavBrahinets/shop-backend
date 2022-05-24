package com.shop.services.basket;

import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
import com.shop.services.BasketService;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
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
    void get_all_baskets() {
        basketService.getBaskets();

        verify(basketRepository).getBaskets();
    }

    @Test
    void add_new_basket() {
        basketService.addBasket(basket, 1);

        verify(basketValidator, atLeast(1)).validate(basket);
        verify(personValidator, atLeast(1)).validate(1);
        verify(basketRepository).addBasket(basket, 1);
    }

    @Test
    void update_basket() {
        basketService.updateBasket(1, basket);

        verify(basketValidator, atLeast(1)).validate(1);
        verify(basketValidator, atLeast(1)).validate(basket);
        verify(basketRepository).updateBasket(1, basket);
    }

    @Test
    void delete_basket_by_id() {
        basketService.deleteBasket(1);

        verify(basketValidator, atLeast(1)).validate(1);
        verify(basketRepository).deleteBasket(1);
    }

    @Test
    void get_basket_by_id() {
        basketService.getBasket(1);

        verify(basketValidator, atLeast(1)).validate(1);
        verify(basketRepository).getBasket(1);
    }

    @Test
    void get_basket_by_person() {
        basketService.getBasketByPerson(1);

        verify(personValidator, atLeast(1)).validate(1);
        verify(basketRepository).getBasketByPerson(1);
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
