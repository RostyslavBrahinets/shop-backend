package com.shop.mvc;

import com.shop.models.User;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.UserService;
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
@WebMvcTest(ProfileViewController.class)
class ProfileViewControllerTest {
    @MockBean
    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Redirect guest from profile to login")
    void redirect_guest_from_profile_to_login() throws Exception {
        mockMvc.perform(get("/profile")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("Show profile for user")
    void show_profile_for_user() throws Exception {
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

        mockMvc.perform(get("/profile")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("profile/index"))
            .andExpect(model().attribute("id", 2L));
    }

    @Test
    @DisplayName("Show profile for admin")
    void show_profile_for_admin() throws Exception {
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

        mockMvc.perform(get("/profile")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("profile/index"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Redirect guest from update profile to login")
    void redirect_guest_from_update_profile_to_login() throws Exception {
        mockMvc.perform(get("/profile/update")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("Show update profile for user")
    void show_update_profile_for_user() throws Exception {
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

        mockMvc.perform(get("/profile/update")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("profile/update"))
            .andExpect(model().attribute("id", 2L));
    }

    @Test
    @DisplayName("Show update profile for admin")
    void show_update_profile_for_admin() throws Exception {
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

        mockMvc.perform(get("/profile/update")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("profile/update"))
            .andExpect(model().attribute("id", 1L));
    }
}
