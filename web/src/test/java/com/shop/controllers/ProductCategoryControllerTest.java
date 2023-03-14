package com.shop.controllers;

import com.shop.product.Product;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.product_category.ProductCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.controllers.ProductCategoryController.PRODUCT_CATEGORY_URL;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(ProductCategoryController.class)
class ProductCategoryControllerTest {
    @Autowired
    @MockBean
    private ProductCategoryService productCategoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All products in category request")
    void all_products_in_category_request() throws Exception {
        when(productCategoryService.findAllProductsInCategory(1)).thenReturn(
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

        mockMvc.perform(get(PRODUCT_CATEGORY_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
}
