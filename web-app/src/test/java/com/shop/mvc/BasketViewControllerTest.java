package com.shop.mvc;

import com.shop.models.Basket;
import com.shop.models.Person;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.BasketService;
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
@WebMvcTest(BasketViewController.class)
class BasketViewControllerTest {
    @MockBean
    @Autowired
    private PersonService personService;
    @MockBean
    @Autowired
    private BasketService basketService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Redirect guest from basket to login")
    void redirect_guest_from_basket_to_login() throws Exception {
        mockMvc.perform(get("/basket")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("Show basket for user")
    void show_basket_for_user() throws Exception {
        when(personService.findByEmail("test@email.com")).thenReturn(
            new Person(2, "John", "Smith")
        );

        when(basketService.findByPerson(2)).thenReturn(
            new Basket(1, 0)
        );

        mockMvc.perform(get("/basket")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("basket/index"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Basket not found for admin")
    void basket_not_found_for_admin() throws Exception {
        when(personService.findByEmail("admin")).thenReturn(
            new Person(1, "admin", "admin")
        );

        mockMvc.perform(get("/basket")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
