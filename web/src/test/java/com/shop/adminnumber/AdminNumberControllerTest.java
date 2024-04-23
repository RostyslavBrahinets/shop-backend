package com.shop.adminnumber;

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

import static com.shop.adminnumber.AdminNumberController.ADMIN_NUMBERS_URL;
import static com.shop.adminnumber.AdminNumberParameter.*;
import static com.shop.category.CategoryParameter.getCategoryWithoutId2;
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
@WebMvcTest(AdminNumberController.class)
class AdminNumberControllerTest {
    @Autowired
    @MockBean
    private AdminNumberService adminNumberService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All admin numbers request")
    void all_admin_numbers_request() throws Exception {
        when(adminNumberService.findAll()).thenReturn(
            List.of(
                getAdminNumberWithoutId()
            )
        );

        mockMvc.perform(get(ADMIN_NUMBERS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Admin number not found because of incorrect id")
    void admin_number_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(ADMIN_NUMBERS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Admin number not found")
    void admin_number_not_found() throws Exception {
        when(adminNumberService.findById(getAdminNumberId()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(ADMIN_NUMBERS_URL + String.format("/%d", getAdminNumberId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Admin number found")
    void admin_number_found() throws Exception {
        when(adminNumberService.findById(getAdminNumberId()))
            .thenReturn(getAdminNumberWithId());

        mockMvc.perform(get(ADMIN_NUMBERS_URL + String.format("/%d", getAdminNumberId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin number saved")
    void admin_number_saved() throws Exception {
        when(adminNumberService.save(getAdminNumberWithoutId()))
            .thenReturn(getAdminNumberWithId());

        mockMvc.perform(post(ADMIN_NUMBERS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getAdminNumberWithoutId())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin number updated")
    void admin_number_updated() throws Exception {
        when(adminNumberService.update(getAdminNumberId(), getAdminNumberWithId2()))
            .thenReturn(getAdminNumberWithId2());

        mockMvc.perform(put(ADMIN_NUMBERS_URL + String.format("/%s", getAdminNumberId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getCategoryWithoutId2())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin number deleted")
    void admin_number_deleted() throws Exception {
        mockMvc.perform(delete(ADMIN_NUMBERS_URL + String.format("/%s", getNumber()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }
}
