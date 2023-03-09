package com.shop.integration.services.contextconfiguration;

import com.shop.models.Cart;
import com.shop.models.Person;
import com.shop.repositories.CartRepository;
import com.shop.services.CartService;
import com.shop.services.PersonService;
import com.shop.validators.CartValidator;
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
    private PersonService personService;
    @Autowired
    private PersonValidator personValidator;
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
    @DisplayName("Get cart by person")
    void get_cart_by_person() {
        long personId = 1;
        List<Person> people = personService.findAll();

        cartService.findByPerson(personId);

        verify(personValidator, atLeast(1)).validate(personId, people);
        verify(cartRepository).findByPerson(personId);
    }

    @Test
    @DisplayName("Save cart")
    void save_cart() {
        long personId = 1;
        double totalCost = 0;
        List<Person> people = personService.findAll();

        cartService.save(totalCost, personId);

        verify(cartValidator, atLeast(1)).validate(totalCost);
        verify(personValidator, atLeast(1)).validate(personId, people);
        verify(cartRepository).save(totalCost, personId);
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
        public PersonService personService() {
            return mock(PersonService.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }
    }
}
