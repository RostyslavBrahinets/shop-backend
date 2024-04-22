package com.shop.category;

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

import static com.shop.category.CategoryController.CATEGORIES_URL;
import static com.shop.category.CategoryParameter.*;
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
                getCategoryWithId()
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
        when(categoryService.findById(getCategoryId()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(CATEGORIES_URL + String.format("/%d", getCategoryId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Category found")
    void category_found() throws Exception {
        when(categoryService.findById(getCategoryId()))
            .thenReturn(getCategoryWithId());

        mockMvc.perform(get(CATEGORIES_URL + String.format("/%d", getCategoryId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Category saved")
    void category_saved() throws Exception {
        when(categoryService.save(getCategoryWithoutId()))
            .thenReturn(getCategoryWithId());

        mockMvc.perform(post(CATEGORIES_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getCategoryWithId())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Category updated")
    void category_updated() throws Exception {
        when(categoryService.update(getCategoryId(), getCategoryWithoutId2()))
            .thenReturn(getCategoryWithId2());

        mockMvc.perform(put(CATEGORIES_URL + String.format("/%s", getCategoryId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getCategoryWithoutId2())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Category deleted")
    void category_deleted() throws Exception {
        mockMvc.perform(delete(CATEGORIES_URL + String.format("/%s", getName()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }
}
