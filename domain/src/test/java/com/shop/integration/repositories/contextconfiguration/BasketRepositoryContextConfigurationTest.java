package com.shop.integration.repositories.contextconfiguration;

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

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        BasketRepository.class,
        BasketRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class BasketRepositoryContextConfigurationTest {
    @Autowired
    private BasketDao basketDao;
    @Autowired
    private BasketRepository basketRepository;

    @Test
    @DisplayName("Get all baskets")
    void get_all_baskets() {
        basketRepository.findAll();

        verify(basketDao, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get basket by id")
    void get_basket_by_id() {
        long id = 1;

        basketRepository.findById(id);

        verify(basketDao).findById(id);
    }

    @Test
    @DisplayName("Get basket by person")
    void get_basket_by_person() {
        long personId = 1;

        basketRepository.findByPerson(personId);

        verify(basketDao).findByPerson(personId);
    }

    @Test
    @DisplayName("Save basket")
    void save_basket() {
        long personId = 1;
        double totalCost = 0;

        basketRepository.save(totalCost, personId);

        verify(basketDao).save(totalCost, personId);
    }

    @Test
    @DisplayName("Update basket")
    void update_basket() {
        long id = 1;
        double totalCost = 100;

        basketRepository.update(id, totalCost);

        verify(basketDao).update(id, totalCost);
    }

    @Test
    @DisplayName("Delete basket")
    void delete_basket() {
        long id = 1;

        basketRepository.delete(id);

        verify(basketDao).delete(id);
    }

    @Test
    @DisplayName("Count baskets")
    void count_baskets() {
        basketRepository.count();

        verify(basketDao, atLeast(1)).findAll();
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
    }
}
