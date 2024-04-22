package com.shop.user;

import com.shop.exceptions.NotFoundException;
import com.shop.security.SignInPasswordAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.user.UserController.USERS_URL;
import static com.shop.user.UserParameter.*;
import static com.shop.utilities.JsonUtility.getJsonBody;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                getUserWithId()
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
        when(userService.findById(getUserId()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(USERS_URL + String.format("/%d", getUserId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("User found")
    void user_found() throws Exception {
        when(userService.findById(getUserId()))
            .thenReturn(getUserWithId());

        mockMvc.perform(get(USERS_URL + String.format("/%d", getUserId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User saved")
    void user_saved() throws Exception {
        when(userService.save(getUserWithoutId()))
            .thenReturn(getUserWithId());

        mockMvc.perform(post(USERS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getUserWithoutId())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User updated")
    void user_updated() throws Exception {
        when(userService.update(getUserId(), getUserWithoutId2()))
            .thenReturn(getUserWithId2());

        mockMvc.perform(put(USERS_URL + String.format("/%s", getUserId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getUserWithoutId2())))
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
        mockMvc.perform(delete(USERS_URL + String.format("/%d", getUserId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is2xxSuccessful());
    }
}
