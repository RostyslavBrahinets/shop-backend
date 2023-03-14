package com.shop.user_role;

import com.shop.exceptions.NotFoundException;
import com.shop.role.Role;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.role.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static com.shop.user_role.UserRoleController.USER_ROLE_URL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class),
    @MockBean(RoleService.class)
})
@WebMvcTest(UserRoleController.class)
class UserRoleControllerTest {
    @Autowired
    @MockBean
    private UserRoleService userRoleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Role for user not found because of incorrect id")
    void role_for_user_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(USER_ROLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Role for user not found")
    void role_for_user_not_found() throws Exception {
        when(userRoleService.findRoleForUser(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(USER_ROLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Role for user found")
    void role_for_user_found() throws Exception {
        when(userRoleService.findRoleForUser(1))
            .thenReturn(new Role(1, "name"));

        mockMvc.perform(get(USER_ROLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Role for user update failed for incorrect id")
    void role_for_user_update_failed_for_incorrect_id() throws Exception {
        mockMvc.perform(post(USER_ROLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User update failed for incorrect id")
    void user_update_failed_for_incorrect_id() throws Exception {
        mockMvc.perform(post(USER_ROLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }
}
