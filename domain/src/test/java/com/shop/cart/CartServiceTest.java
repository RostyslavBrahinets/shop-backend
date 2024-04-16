package com.shop.cart;

import com.shop.user.UserService;
import com.shop.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.shop.cart.CartParameter.*;
import static com.shop.user.UserParameter.getUserWithId;
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
    private UserService userService;
    @Mock
    private UserValidator userValidator;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartService = new CartService(
            cartRepository,
            cartValidator,
            userService,
            userValidator
        );
    }

    @Test
    @DisplayName("Empty list of carts is returned in case when no carts in storage")
    void empty_list_of_carts_is_returned_in_case_when_no_carts_in_storage() {
        when(cartRepository.findAll()).thenReturn(emptyList());

        List<Cart> carts = cartService.findAll();

        assertThat(carts).isEmpty();
    }

    @Test
    @DisplayName("List of carts is returned in case when carts are exists in storage")
    void list_of_carts_is_returned_in_case_when_carts_are_exists_in_storage() {
        when(cartRepository.findAll()).thenReturn(
            List.of(getCartWithId())
        );

        List<Cart> carts = cartService.findAll();

        assertThat(carts).isEqualTo(List.of(getCartWithId()));
    }

    @Test
    @DisplayName("Cart was found by id")
    void cart_was_found_by_id() {
        when(cartRepository.findById(1)).thenReturn(
            Optional.of(getCartWithId())
        );

        Cart cart = cartService.findById(getCartId());

        assertThat(cart).isEqualTo(getCartWithId());
    }

    @Test
    @DisplayName("Cart was saved for with correct input")
    void cart_was_saved_with_correct_input() {
        Cart savedCart = cartService.save(getCartWithoutId());

        verify(cartRepository).save(getCartWithId());

        assertThat(savedCart).isEqualTo(getCartWithId());
    }

    @Test
    @DisplayName("Cart was deleted")
    void cart_was_deleted() {
        cartService.delete(getCartWithId());
        verify(cartRepository).delete(getCartWithId());
    }

    @Test
    @DisplayName("Cart was found by user")
    void cart_was_found_by_user() {
        when(cartRepository.findByUser(getUserWithId())).thenReturn(
            Optional.of(getCartWithId())
        );

        Cart cart = cartService.findByUser(getUserWithId());

        assertThat(cart).isEqualTo(getCartWithId());
    }
}
