package com.shop.mvc;

import com.shop.models.Cart;
import com.shop.models.Person;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.CartService;
import com.shop.services.PersonService;
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
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(CartViewController.class)
class CartViewControllerTest {
    @MockBean
    @Autowired
    private PersonService personService;
    @MockBean
    @Autowired
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Redirect guest from cart to login")
    void redirect_guest_from_cart_to_login() throws Exception {
        mockMvc.perform(get("/cart")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("Show cart for user")
    void show_cart_for_user() throws Exception {
        when(personService.findByEmail("test@email.com")).thenReturn(
            new Person(2, "John", "Smith")
        );

        when(cartService.findByPerson(2)).thenReturn(
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
        when(personService.findByEmail("admin")).thenReturn(
            new Person(1, "admin", "admin")
        );

        mockMvc.perform(get("/cart")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
