package com.shop.product;

import com.shop.exceptions.NotFoundException;
import com.shop.security.SignInPasswordAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.product.ProductController.PRODUCTS_URL;
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
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All products request")
    void all_products_request() throws Exception {
        when(productService.findAll()).thenReturn(
            List.of(
                new Product(
                    1,
                    "name",
                    "describe",
                    100,
                    "123",
                    true, new byte[]{1, 1, 1}
                )
            )
        );

        mockMvc.perform(get(PRODUCTS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Product not found because of incorrect id")
    void product_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(PRODUCTS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Product not found")
    void product_not_found() throws Exception {
        when(productService.findById(anyInt()))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(get(PRODUCTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Product found")
    void product_found() throws Exception {
        when(productService.findById(1))
            .thenReturn(
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

        mockMvc.perform(get(PRODUCTS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Product deleted")
    void product_deleted() throws Exception {
        mockMvc.perform(post(PRODUCTS_URL + "/123")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Image for product not found because of incorrect id")
    void image_for_product_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(PRODUCTS_URL + "/image/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Image for product found")
    void image_for_product_found() throws Exception {
        when(productService.findById(1))
            .thenReturn(
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

        mockMvc.perform(get(PRODUCTS_URL + "/image/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }
}
