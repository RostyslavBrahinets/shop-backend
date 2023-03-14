package com.shop.cart;

import com.shop.cart.Cart;
import com.shop.cart.CartViewController;
import com.shop.user.User;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.cart.CartService;
import com.shop.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(CartViewController.class)
class CartViewControllerTest {
    @MockBean
    @Autowired
    private UserService userService;
    @MockBean
    @Autowired
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Redirect guest from cart to sign in")
    void redirect_guest_from_cart_to_sign_in() throws Exception {
        mockMvc.perform(get("/cart")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));
    }

    @Test
    @DisplayName("Show cart for user")
    void show_cart_for_user() throws Exception {
        when(userService.findByEmail("test@email.com"))
            .thenReturn(
                new User(
                    2,
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    2
                )
            );

        when(cartService.findByUser(2)).thenReturn(
            new Cart(1, 0)
        );

        mockMvc.perform(get("/cart")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("cart/index"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Cart not found for admin")
    void cart_not_found_for_admin() throws Exception {
        when(userService.findByEmail("admin"))
            .thenReturn(
                new User(
                    1,
                    "admin",
                    "admin",
                    "admin",
                    "",
                    "password",
                    1
                )
            );

        mockMvc.perform(get("/cart")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
