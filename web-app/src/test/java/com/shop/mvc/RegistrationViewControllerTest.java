package com.shop.mvc;

import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.*;
import com.shop.stripe.StripePayment;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class),
    @MockBean(ContactService.class),
    @MockBean(PersonRoleService.class),
    @MockBean(CartService.class),
    @MockBean(PersonValidator.class),
    @MockBean(ContactValidator.class),
    @MockBean(StripePayment.class)
})
@WebMvcTest(RegistrationViewController.class)
class RegistrationViewControllerTest {
//    @MockBean
//    @Autowired
//    private PersonService personService;
//    @MockBean
//    @Autowired
//    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Show registration page for guest")
    void show_registration_page_for_guest() throws Exception {
        mockMvc.perform(get("/registration")
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("registration"));
    }

    @Test
    @DisplayName("Redirect from registration page to main page for user")
    void redirect_from_registration_page_to_main_page_for_user() throws Exception {
        mockMvc.perform(get("/registration")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("Redirect from registration page to main page for admin")
    void redirect_from_registration_page_to_main_page_for_admin() throws Exception {
        mockMvc.perform(get("/registration")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
