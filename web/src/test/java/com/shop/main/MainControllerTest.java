package com.shop.main;

import com.shop.product.Product;
import com.shop.product.ProductService;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(MainController.class)
class MainControllerTest {
    @Autowired
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Random products request for admin")
    void random_products_request_for_admin() throws Exception {
        when(productService.findRandomProducts(1)).thenReturn(
            List.of(
                new Product(
                    1,
                    "name",
                    "describe",
                    100,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
            )
        );

        mockMvc.perform(get(MainController.MAIN_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Random products request for user")
    void random_products_request_for_user() throws Exception {
        when(productService.findRandomProducts(1)).thenReturn(
            List.of(
                new Product(
                    1,
                    "name",
                    "describe",
                    100,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
            )
        );

        mockMvc.perform(get(MainController.MAIN_URL)
                .with(user("user@email.com").password("password").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Random products request for guest")
    void random_products_request_for_guest() throws Exception {
        when(productService.findRandomProducts(1)).thenReturn(
            List.of(
                new Product(
                    1,
                    "name",
                    "describe",
                    100,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
            )
        );

        mockMvc.perform(
                get(MainController.MAIN_URL)
                    .with(anonymous())
                    .with(user("username").roles("ANONYMOUS"))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
}
