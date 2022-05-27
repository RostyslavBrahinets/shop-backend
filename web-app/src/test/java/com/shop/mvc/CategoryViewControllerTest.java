package com.shop.mvc;

import com.shop.security.LoginPasswordAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(CategoryViewController.class)
class CategoryViewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Show categories for guest")
    void show_categories_for_guest() throws Exception {
        mockMvc.perform(get("/categories")
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/index"))
            .andExpect(model().attribute("role", "ROLE_GUEST"));
    }

    @Test
    @DisplayName("Show categories for user")
    void show_categories_for_user() throws Exception {
        mockMvc.perform(get("/categories")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/index"))
            .andExpect(model().attribute("role", "ROLE_USER"));
    }

    @Test
    @DisplayName("Show categories for admin")
    void show_categories_for_admin() throws Exception {
        mockMvc.perform(get("/categories")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/index"))
            .andExpect(model().attribute("role", "ROLE_ADMIN"));
        ;
    }

    @Test
    @DisplayName("Category not found because incorrect id")
    void category_not_found_because_incorrect_id() throws Exception {
        mockMvc.perform(get("/categories/id")
                .with(anonymous()))
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/categories/id")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/categories/id")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Show products in category for guest")
    void show_products_in_category_for_guest() throws Exception {
        mockMvc.perform(get("/categories/1")
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/find"))
            .andExpect(model().attribute("role", "ROLE_GUEST"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Show products in category for user")
    void show_products_in_category_for_user() throws Exception {
        mockMvc.perform(get("/categories/1")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/find"))
            .andExpect(model().attribute("role", "ROLE_USER"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Show products in category for admin")
    void show_products_in_category_for_admin() throws Exception {
        mockMvc.perform(get("/categories/1")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/find"))
            .andExpect(model().attribute("role", "ROLE_ADMIN"))
            .andExpect(model().attribute("id", 1L));
    }
}
