package com.shop.main;

import com.shop.main.MainViewController;
import com.shop.user.User;
import com.shop.security.SignInPasswordAuthenticationProvider;
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
@WebMvcTest(MainViewController.class)
class MainViewControllerTest {
    @MockBean
    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Show main page for guest")
    void show_main_page_for_guest() throws Exception {
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
    void show_main_page_for_user() throws Exception {
        when(userService.findByEmail("test@email.com")).thenReturn(
            new User(
                2,
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                null
            )
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
    void show_main_page_for_admin() throws Exception {
        when(userService.findByEmail("admin"))
            .thenReturn(
                new User(
                    1,
                    "admin",
                    "admin",
                    "admin",
                    "",
                    new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                )
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
