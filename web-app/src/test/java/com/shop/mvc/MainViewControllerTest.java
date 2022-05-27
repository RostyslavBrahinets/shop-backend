package com.shop.mvc;

import com.shop.models.Person;
import com.shop.security.LoginPasswordAuthenticationProvider;
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
@WebMvcTest(MainViewController.class)
class MainViewControllerTest {
    @MockBean
    @Autowired
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Show main page for guest")
    void show_root_page_for_guest() throws Exception {
        mockMvc.perform(get("/")
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("index"))
            .andExpect(model().attribute("id", 0L))
            .andExpect(model().attribute("role", "ROLE_GUEST"));
    }

    @Test
    @DisplayName("Show main page for user")
    void show_root_page_for_user() throws Exception {
        when(personService.findByEmail("test@email.com")).thenReturn(
            new Person(2, "John", "Smith")
        );

        mockMvc.perform(get("/")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("index"))
            .andExpect(model().attribute("id", 2L))
            .andExpect(model().attribute("role", "ROLE_USER"));
    }

    @Test
    @DisplayName("Show main page for admin")
    void show_root_page_for_admin() throws Exception {
        when(personService.findByEmail("admin")).thenReturn(
            new Person(1, "admin", "admin")
        );

        mockMvc.perform(get("/")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("index"))
            .andExpect(model().attribute("id", 1L))
            .andExpect(model().attribute("role", "ROLE_ADMIN"));
    }
}
