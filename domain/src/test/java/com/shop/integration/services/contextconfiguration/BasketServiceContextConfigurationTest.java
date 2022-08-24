package com.shop.integration.services.contextconfiguration;

import com.shop.models.Basket;
import com.shop.models.Person;
import com.shop.repositories.BasketRepository;
import com.shop.services.BasketService;
import com.shop.services.PersonService;
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

import java.util.List;

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
    private BasketRepository basketRepository;
    @Autowired
    private BasketValidator basketValidator;
    @Autowired
    private PersonService personService;
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
    @DisplayName("Get basket by id")
    void get_basket_by_id() {
        long id = 1;

        basketService.findById(id);

        verify(basketValidator, atLeast(1)).validate(id);
        verify(basketRepository).findById(id);
    }

    @Test
    @DisplayName("Get basket by person")
    void get_basket_by_person() {
        long personId = 1;
        List<Person> people = personService.findAll();

        basketService.findByPerson(personId);

        verify(personValidator, atLeast(1)).validate(personId, people);
        verify(basketRepository).findByPerson(personId);
    }

    @Test
    @DisplayName("Save basket")
    void save_basket() {
        long personId = 1;
        double totalCost = 0;
        List<Person> people = personService.findAll();

        basketService.save(totalCost, personId);

        verify(basketValidator, atLeast(1)).validate(totalCost);
        verify(personValidator, atLeast(1)).validate(personId, people);
        verify(basketRepository).save(totalCost, personId);
    }

    @Test
    @DisplayName("Update basket")
    void update_basket() {
        long id = 1;
        double totalCost = 100;

        basketService.update(id, totalCost);

        verify(basketValidator, atLeast(1)).validate(id);
        verify(basketValidator, atLeast(1)).validate(totalCost);
        verify(basketRepository).update(1, totalCost);
    }

    @Test
    @DisplayName("Delete basket")
    void delete_basket() {
        long id = 1;

        basketService.delete(id);

        verify(basketValidator, atLeast(1)).validate(id);
        verify(basketRepository).delete(id);
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
        public PersonService personService() {
            return mock(PersonService.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }
    }
}
