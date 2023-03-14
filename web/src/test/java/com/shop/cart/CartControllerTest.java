package com.shop.cart;

import com.shop.cart.CartController;
import com.shop.exceptions.NotFoundException;
import com.shop.cart.Cart;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.cart.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.cart.CartController.CARTS_URL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(CartController.class)
class CartControllerTest {
    @Autowired
    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All carts request")
    void all_carts_request() throws Exception {
        when(cartService.findAll()).thenReturn(
            List.of(
                new Cart(1, 0)
            )
        );

        mockMvc.perform(get(CARTS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Cart not found because of incorrect id")
    void cart_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(CARTS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cart not found")
    void cart_not_found() throws Exception {
        when(cartService.findById(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(CARTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cart found")
    void cart_found() throws Exception {
        when(cartService.findById(1))
            .thenReturn(new Cart(1, 0));

        mockMvc.perform(get(CARTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cart not deleted because of incorrect id")
    void cart_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(delete(CARTS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cart deleted")
    void cart_deleted() throws Exception {
        mockMvc.perform(delete(CARTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is2xxSuccessful());
    }
}
