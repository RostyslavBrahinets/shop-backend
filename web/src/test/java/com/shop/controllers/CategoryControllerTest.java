package com.shop.controllers;

import com.shop.category.Category;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static com.shop.controllers.CategoryController.CATEGORIES_URL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All categories request")
    void all_categories_request() throws Exception {
        when(categoryService.findAll()).thenReturn(
            List.of(
                new Category(1, "name")
            )
        );

        mockMvc.perform(get(CATEGORIES_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Category not found because of incorrect id")
    void category_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(CATEGORIES_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Category not found")
    void category_not_found() throws Exception {
        when(categoryService.findById(anyInt()))
            .thenThrow(NoSuchElementException.class);

        mockMvc.perform(get(CATEGORIES_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Category found")
    void category_found() throws Exception {
        when(categoryService.findById(1))
            .thenReturn(new Category(1, "name"));

        mockMvc.perform(get(CATEGORIES_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Category deleted")
    void category_deleted() throws Exception {
        mockMvc.perform(post(CATEGORIES_URL + "/name")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }
}
