package com.shop.controllers;

import com.shop.exceptions.NotFoundException;
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

import java.util.List;

import static com.shop.controllers.PersonController.PEOPLE_URL;
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
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    @MockBean
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All people request")
    void all_people_request() throws Exception {
        when(personService.findAll()).thenReturn(
            List.of(
                new Person(1, "John", "Smith")
            )
        );

        mockMvc.perform(get(PEOPLE_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Person not found because of incorrect id")
    void person_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(PEOPLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Person not found")
    void person_not_found() throws Exception {
        when(personService.findById(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(PEOPLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Person found")
    void person_found() throws Exception {
        when(personService.findById(1))
            .thenReturn(new Person(1, "John", "Smith"));

        mockMvc.perform(get(PEOPLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Person not deleted because of incorrect id")
    void person_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(delete(PEOPLE_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Person deleted")
    void person_deleted() throws Exception {
        mockMvc.perform(delete(PEOPLE_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is2xxSuccessful());
    }
}
