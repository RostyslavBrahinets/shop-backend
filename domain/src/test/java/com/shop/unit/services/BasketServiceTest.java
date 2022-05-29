package com.shop.unit.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
import com.shop.services.BasketService;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BasketServiceTest {
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private BasketValidator basketValidator;
    @Mock
    private PersonValidator personValidator;

    private BasketService basketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        basketService = new BasketService(
            basketRepository,
            basketValidator,
            personValidator
        );
    }

    @Test
    @DisplayName("Basket was saved for with correct input")
    void basket_was_saved_with_correct_input() {
        when(basketRepository.save(0, 1))
            .thenReturn(Basket.of(0).withId(1));

        Basket savedBasket = basketService.save(0, 1);

        assertThat(savedBasket).isEqualTo(new Basket(1, 0));
    }

    @Test
    @DisplayName("Basket was not saved for with incorrect input")
    void basket_was_not_saved_with_incorrect_input() {
        when(basketRepository.save(0, 1))
            .thenReturn(Basket.of(0).withId(1));

        Basket savedBasket = basketService.save(0, 1);

        assertThat(savedBasket).isEqualTo(new Basket(1, 0));
    }

    @Test
    @DisplayName("Empty list of baskets is returned in case when no baskets in storage")
    void empty_list_of_baskets_is_returned_in_case_when_no_baskets_in_storage() {
        when(basketRepository.findAll()).thenReturn(emptyList());

        List<Basket> baskets = basketService.findAll();

        assertThat(baskets).isEmpty();
    }

    @Test
    @DisplayName("List of baskets is returned in case when baskets are exists in storage")
    void list_of_baskets_is_returned_in_case_when_baskets_are_exists_in_storage() {
        when(basketRepository.findAll()).thenReturn(
            List.of(
                Basket.of(0).withId(1)
            )
        );

        List<Basket> baskets = basketService.findAll();

        assertThat(baskets).isEqualTo(List.of(new Basket(1, 0)));
    }

    @Test
    @DisplayName("Basket was found by id")
    void basket_was_found_by_id() {
        when(basketRepository.findById(1)).thenReturn(
            Optional.of(Basket.of(0).withId(1))
        );

        Basket basket = basketService.findById(1);

        assertThat(basket).isEqualTo(new Basket(1, 0));
    }

    @Test
    @DisplayName("Basket was found by person")
    void basket_was_found_by_person() {
        when(basketRepository.findByPerson(1)).thenReturn(
            Optional.of(Basket.of(0).withId(1))
        );

        Basket basket = basketService.findByPerson(1);

        assertThat(basket).isEqualTo(new Basket(1, 0));
    }

    @Test
    @DisplayName("Basket was deleted")
    void basket_was_deleted() {
        basketService.delete(1);
        verify(basketRepository).delete(1);
    }
}
