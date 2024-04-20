package com.shop.productcategory;

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

import static com.shop.category.CategoryParameter.getCategoryId;
import static com.shop.product.ProductParameter.getProductWithId;
import static com.shop.productcategory.ProductCategoryController.PRODUCT_CATEGORY_URL;
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
        when(productCategoryService.findAllProductsInCategory(getCategoryId()))
            .thenReturn(
                List.of(
                    getProductWithId()
                )
            );

        mockMvc.perform(get(PRODUCT_CATEGORY_URL + String.format("/%d", getCategoryId()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
}
