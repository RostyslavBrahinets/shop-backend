package com.shop.repositories.contextconfiguration;

import com.shop.dao.BasketDao;
import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
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
        BasketRepository.class,
        BasketRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class BasketRepositoryContextConfigurationTest {
    @Autowired
    private Basket basket;
    @Autowired
    private BasketDao basketDao;
    @Autowired
    private BasketRepository basketRepository;

    @Test
    @DisplayName("Get all baskets")
    void get_all_baskets() {
        basketRepository.findAll();

        verify(basketDao).findAll();
    }

    @Test
    @DisplayName("Save basket")
    void save_basket() {
        basketRepository.save(basket, 1);

        verify(basketDao).save(basket.getTotalCost(), 1);
    }

    @Test
    @DisplayName("Update basket")
    void update_basket() {
        basketRepository.update(1, basket);

        verify(basketDao).update(1, basket.getTotalCost());
    }

    @Test
    @DisplayName("Delete basket")
    void delete_basket() {
        basketRepository.delete(1);

        verify(basketDao).delete(1);
    }

    @Test
    @DisplayName("Get basket by id")
    void get_basket_by_id() {
        basketRepository.findById(1);

        verify(basketDao).findById(1);
    }

    @Test
    @DisplayName("Get basket by person")
    void get_basket_by_person() {
        basketRepository.findByPerson(1);

        verify(basketDao).findByPerson(1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Basket basket() {
            return mock(Basket.class);
        }

        @Bean
        public BasketDao basketDao() {
            return mock(BasketDao.class);
        }

        @Bean
        public BasketRepository basketRepository() {
            return mock(BasketRepository.class);
        }
    }
}
