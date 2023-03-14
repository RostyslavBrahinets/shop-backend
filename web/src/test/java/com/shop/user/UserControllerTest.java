package com.shop.user;

import com.shop.exceptions.NotFoundException;
import com.shop.user.User;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.user.UserController;
import com.shop.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.user.UserController.USERS_URL;
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
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All users request")
    void all_users_request() throws Exception {
        when(userService.findAll()).thenReturn(
            List.of(
                new User(
                    1,
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    1
                )
            )
        );

        mockMvc.perform(get(USERS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("User not found because of incorrect id")
    void user_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(USERS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User not found")
    void user_not_found() throws Exception {
        when(userService.findById(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(USERS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User found")
    void user_found() throws Exception {
        when(userService.findById(1))
            .thenReturn(
                new User(
                    1,
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    1
                )
            );

        mockMvc.perform(get(USERS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User not deleted because of incorrect id")
    void user_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(delete(USERS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User deleted")
    void user_deleted() throws Exception {
        mockMvc.perform(delete(USERS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is2xxSuccessful());
    }
}
