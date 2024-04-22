package com.shop.productcategory;

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

import static com.shop.category.CategoryParameter.getCategoryId;
import static com.shop.category.CategoryParameter.getCategoryWithId;
import static com.shop.product.ProductParameter.getBarcode;
import static com.shop.product.ProductParameter.getProductWithId;
import static com.shop.productcategory.ProductCategoryController.PRODUCT_CATEGORY_URL;
import static com.shop.productcategory.ProductCategoryParameter.getProductCategory;
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

    @Test
    @DisplayName("Category for product found")
    void category_for_product_found() throws Exception {
        when(productCategoryService.findCategoryForProduct(getBarcode()))
            .thenReturn(getCategoryWithId());

        mockMvc.perform(get(PRODUCT_CATEGORY_URL + String.format("/%s", getBarcode()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Product to category saved")
    void product_to_category_saved() throws Exception {
        mockMvc.perform(post(PRODUCT_CATEGORY_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getProductCategory())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Product to category updated")
    void product_to_category_updated() throws Exception {
        mockMvc.perform(put(PRODUCT_CATEGORY_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonBody(getProductCategory())))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Product from category deleted")
    void product_from_category_deleted() throws Exception {
        mockMvc.perform(delete(PRODUCT_CATEGORY_URL + String.format("/%s", getBarcode()))
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }
}
