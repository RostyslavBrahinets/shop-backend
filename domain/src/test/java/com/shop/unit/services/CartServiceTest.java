package com.shop.unit.services;

import com.shop.models.Cart;
import com.shop.repositories.CartRepository;
import com.shop.services.CartService;
import com.shop.services.PersonService;
import com.shop.validators.CartValidator;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartValidator cartValidator;
    @Mock
    private PersonService personService;
    @Mock
    private PersonValidator personValidator;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartService = new CartService(
            cartRepository,
            cartValidator,
            personService,
            personValidator
        );
    }

    @Test
    @DisplayName("Cart was saved for with correct input")
    void cart_was_saved_with_correct_input() {
        when(cartRepository.save(0, 1))
            .thenReturn(Cart.of(0).withId(1));

        Cart savedCart = cartService.save(0, 1);

        assertThat(savedCart).isEqualTo(new Cart(1, 0));
    }

    @Test
    @DisplayName("Cart was not saved for with incorrect input")
    void cart_was_not_saved_with_incorrect_input() {
        when(cartRepository.save(0, 1))
            .thenReturn(Cart.of(0).withId(1));

        Cart savedCart = cartService.save(0, 1);

        assertThat(savedCart).isEqualTo(new Cart(1, 0));
    }

    @Test
    @DisplayName("Empty list of carts is returned in case when no carts in storage")
    void empty_list_of_bcarts_is_returned_in_case_when_no_carts_in_storage() {
        when(cartRepository.findAll()).thenReturn(emptyList());

        List<Cart> carts = cartService.findAll();

        assertThat(carts).isEmpty();
    }

    @Test
    @DisplayName("List of carts is returned in case when carts are exists in storage")
    void list_of_carts_is_returned_in_case_when_carts_are_exists_in_storage() {
        when(cartRepository.findAll()).thenReturn(
            List.of(
                Cart.of(0).withId(1)
            )
        );

        List<Cart> carts = cartService.findAll();

        assertThat(carts).isEqualTo(List.of(new Cart(1, 0)));
    }

    @Test
    @DisplayName("Cart was found by id")
    void cart_was_found_by_id() {
        when(cartRepository.findById(1)).thenReturn(
            Optional.of(Cart.of(0).withId(1))
        );

        Cart cart = cartService.findById(1);

        assertThat(cart).isEqualTo(new Cart(1, 0));
    }

    @Test
    @DisplayName("Cart was found by person")
    void cart_was_found_by_person() {
        when(cartRepository.findByPerson(1)).thenReturn(
            Optional.of(Cart.of(0).withId(1))
        );

        Cart cart = cartService.findByPerson(1);

        assertThat(cart).isEqualTo(new Cart(1, 0));
    }

    @Test
    @DisplayName("Cart was deleted")
    void cart_was_deleted() {
        cartService.delete(1);
        verify(cartRepository).delete(1);
    }
}
