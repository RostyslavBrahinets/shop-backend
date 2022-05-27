package com.shop.controllers;

import com.shop.models.Basket;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.BasketService;
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
import java.util.NoSuchElementException;

import static com.shop.controllers.BasketController.BASKETS_URL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(BasketController.class)
class BasketControllerTest {
    @Autowired
    @MockBean
    private BasketService basketService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All baskets request")
    void all_baskets_request() throws Exception {
        when(basketService.findAll()).thenReturn(
            List.of(
                new Basket(1, 0)
            )
        );

        mockMvc.perform(get(BASKETS_URL)
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Note saved")
    void note_saved() throws Exception {
        mockMvc.perform(
            post(BASKETS_URL)
                .param("totalCost", "0")
                .param("personId", "1")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Basket not found because of incorrect id")
    void basket_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(BASKETS_URL + "/id"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Basket not found")
    void basket_not_found() throws Exception {
        when(basketService.findById(anyInt()))
            .thenThrow(NoSuchElementException.class);

        mockMvc.perform(get(BASKETS_URL + "/1"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Basket found")
    void basket_found() throws Exception {
        when(basketService.findById(1))
            .thenReturn(new Basket(1, 0));

        mockMvc.perform(get(BASKETS_URL + "/1"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Basket not deleted because of incorrect id")
    void basket_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(delete(BASKETS_URL + "/id"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Basket deleted")
    void basket_deleted() throws Exception {
        mockMvc.perform(delete(BASKETS_URL + "/1"))
            .andExpect(status().is3xxRedirection());
    }
}
