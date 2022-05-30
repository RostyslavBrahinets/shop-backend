package com.shop.controllers;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Contact;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.controllers.ContactController.CONTACTS_URL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(ContactController.class)
class ContactControllerTest {
    @Autowired
    @MockBean
    private ContactService contactService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All contacts request")
    void all_contacts_request() throws Exception {
        when(contactService.findAll()).thenReturn(
            List.of(
                new Contact(1, "test@email.com", "+380000000000", "password")
            )
        );

        mockMvc.perform(get(CONTACTS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Contact not found because of incorrect id")
    void contact_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(CONTACTS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Contact not found")
    void contact_not_found() throws Exception {
        when(contactService.findById(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(CONTACTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Contact found")
    void contact_found() throws Exception {
        when(contactService.findById(1))
            .thenReturn(new Contact(1, "test@email.com", "+380000000000", "password"));

        mockMvc.perform(get(CONTACTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Contact not deleted because of incorrect id")
    void contact_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(delete(CONTACTS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Contact deleted")
    void contact_deleted() throws Exception {
        mockMvc.perform(delete(CONTACTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is2xxSuccessful());
    }


    @Test
    @DisplayName("Contact update failed for incorrect id")
    void contact_update_failed_for_incorrect_id() throws Exception {
        mockMvc.perform(post(CONTACTS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }
}
