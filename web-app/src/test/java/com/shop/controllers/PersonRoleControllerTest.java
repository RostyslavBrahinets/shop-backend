package com.shop.controllers;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Role;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.PersonRoleService;
import com.shop.services.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static com.shop.controllers.PersonRoleController.PERSON_ROLE_URL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class),
    @MockBean(RoleService.class)
})
@WebMvcTest(PersonRoleController.class)
class PersonRoleControllerTest {
    @Autowired
    @MockBean
    private PersonRoleService personRoleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Role for person not found because of incorrect id")
    void role_for_person_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(PERSON_ROLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Role for person not found")
    void role_for_person_not_found() throws Exception {
        when(personRoleService.findRoleForPerson(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(PERSON_ROLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Role for person found")
    void role_for_person_found() throws Exception {
        when(personRoleService.findRoleForPerson(1))
            .thenReturn(new Role(1, "name"));

        mockMvc.perform(get(PERSON_ROLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Role for person update failed for incorrect id")
    void role_for_person_update_failed_for_incorrect_id() throws Exception {
        mockMvc.perform(post(PERSON_ROLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Person update failed for incorrect id")
    void person_update_failed_for_incorrect_id() throws Exception {
        mockMvc.perform(post(PERSON_ROLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }
}
