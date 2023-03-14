package com.shop.admin;

import com.shop.admin.AdminViewController;
import com.shop.category.Category;
import com.shop.user.User;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.category.CategoryService;
import com.shop.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(AdminViewController.class)
class AdminViewControllerTest {
    @MockBean
    @Autowired
    private UserService userService;
    @MockBean
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Redirect from admin/* to sign in for guest")
    void redirect_from_admin_users_to_sign_in_guest() throws Exception {
        mockMvc.perform(get("/admin/users")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));

        mockMvc.perform(get("/admin/users/1")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));

        mockMvc.perform(get("/admin/users/1/update-role")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));

        mockMvc.perform(get("/admin/products/add")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));

        mockMvc.perform(get("/admin/products/delete")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));

        mockMvc.perform(get("/admin/categories/add")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));

        mockMvc.perform(get("/admin/categories/delete")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));
    }

    @Test
    @DisplayName("Forbidden admin/* for user")
    void forbidden_admin_users_for_user() throws Exception {
        when(userService.findByEmail("test@email.com"))
            .thenReturn(
                new User(
                    2,
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    2
                )
            );

        mockMvc.perform(get("/admin/users")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/users/2")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/users/2/update-role")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/products/add")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/products/delete")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/categories/add")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/categories/delete")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Show all users for admin")
    void show_all_users_for_admin() throws Exception {
        when(userService.findByEmail("admin"))
            .thenReturn(
                new User(
                    1,
                    "admin",
                    "admin",
                    "admin",
                    "",
                    "password",
                    1
                )
            );

        mockMvc.perform(get("/admin/users")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("admin/index"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Show user by id for admin")
    void show_user_by_id_for_admin() throws Exception {
        when(userService.findByEmail("admin"))
            .thenReturn(
                new User(
                    1,
                    "admin",
                    "admin",
                    "admin",
                    "",
                    "password",
                    1
                )
            );

        mockMvc.perform(get("/admin/users/1")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("admin/find"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Show update role of user by id for admin")
    void show_update_role_of_user_by_id_for_admin() throws Exception {
        mockMvc.perform(get("/admin/users/1/update-role")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("admin/update_role"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Show add product for admin")
    void show_add_product_for_admin() throws Exception {
        when(categoryService.findAll()).thenReturn(
            List.of(new Category(1, "name"))
        );

        mockMvc.perform(get("/admin/products/add")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/add"))
            .andExpect(model().attribute("categories",
                List.of("name")
            ));
    }

    @Test
    @DisplayName("Show delete product for admin")
    void show_delete_product_for_admin() throws Exception {
        mockMvc.perform(get("/admin/products/delete")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/delete"));
    }

    @Test
    @DisplayName("Show add category for admin")
    void show_add_category_for_admin() throws Exception {
        mockMvc.perform(get("/admin/categories/add")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/add"));
    }

    @Test
    @DisplayName("Show delete category for admin")
    void show_delete_category_for_admin() throws Exception {
        mockMvc.perform(get("/admin/categories/delete")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("categories/delete"));
    }
}
