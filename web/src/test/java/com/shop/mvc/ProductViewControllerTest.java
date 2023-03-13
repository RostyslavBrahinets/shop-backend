package com.shop.mvc;

import com.shop.product.Product;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(ProductViewController.class)
class ProductViewControllerTest {
    @MockBean
    @Autowired
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Show products for guest")
    void show_products_for_guest() throws Exception {
        mockMvc.perform(get("/products")
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/index"))
            .andExpect(model().attribute("role", "ROLE_GUEST"));
    }

    @Test
    @DisplayName("Show products for user")
    void show_products_for_user() throws Exception {
        mockMvc.perform(get("/products")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/index"))
            .andExpect(model().attribute("role", "ROLE_USER"));
    }

    @Test
    @DisplayName("Show products for admin")
    void show_products_for_admin() throws Exception {
        mockMvc.perform(get("/products")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/index"))
            .andExpect(model().attribute("role", "ROLE_ADMIN"));
        ;
    }

    @Test
    @DisplayName("Product not found because incorrect id")
    void product_not_found_because_incorrect_id() throws Exception {
        mockMvc.perform(get("/products/id")
                .with(anonymous()))
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/products/id")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/products/id")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Show product for guest")
    void show_product_for_guest() throws Exception {
        when(productService.findById(1)).thenReturn(
            new Product(
                1,
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );

        mockMvc.perform(get("/products/1")
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/find"))
            .andExpect(model().attribute("role", "ROLE_GUEST"))
            .andExpect(model().attribute("id", 1L))
            .andExpect(model().attribute("inStock", true));
    }

    @Test
    @DisplayName("Show product for user")
    void show_product_for_user() throws Exception {
        when(productService.findById(1)).thenReturn(
            new Product(
                1,
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );

        mockMvc.perform(get("/products/1")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/find"))
            .andExpect(model().attribute("role", "ROLE_USER"))
            .andExpect(model().attribute("id", 1L))
            .andExpect(model().attribute("inStock", true));
    }

    @Test
    @DisplayName("Show product for admin")
    void show_product_for_admin() throws Exception {
        when(productService.findById(1)).thenReturn(
            new Product(
                1,
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );

        mockMvc.perform(get("/products/1")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("products/find"))
            .andExpect(model().attribute("role", "ROLE_ADMIN"))
            .andExpect(model().attribute("id", 1L))
            .andExpect(model().attribute("inStock", true));
    }
}
