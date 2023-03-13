package com.shop.controllers;

import com.shop.dto.ReportDto;
import com.shop.models.Cart;
import com.shop.models.User;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.CartService;
import com.shop.services.UserService;
import com.shop.services.ProductsCartsService;
import com.shop.services.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.controllers.ProductsCartsController.PRODUCTS_CARTS_URL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class),
    @MockBean(ReportDto.class)
})
@WebMvcTest(ProductsCartsController.class)
class ProductsCartsControllerTest {
    @Autowired
    @MockBean
    private ProductsCartsService productsCartsService;
    @Autowired
    @MockBean
    private CartService cartService;
    @Autowired
    @MockBean
    private UserService userService;
    @Autowired
    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All products from cart request")
    void all_products_from_cart_request() throws Exception {
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

        when(cartService.findByUser(2))
            .thenReturn(new Cart(1, 0));

        when(productsCartsService.findAllProductsInCart(1))
            .thenReturn(
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

        mockMvc.perform(get(PRODUCTS_CARTS_URL)
                .with(user("John").password("password").roles("USER"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Save product to cart")
    void save_product_to_cart() throws Exception {
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

        when(cartService.findByUser(2))
            .thenReturn(new Cart(1, 0));

        when(productsCartsService.saveProductToCart(1, 1))
            .thenReturn(1L);

        mockMvc.perform(post(PRODUCTS_CARTS_URL + "/1")
                .with(user("John").password("password").roles("USER"))
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/cart"));

        verify(productsCartsService).saveProductToCart(1, 1);
    }

    @Test
    @DisplayName("Product from cart not deleted because of incorrect id")
    void product_from_cart_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(post(PRODUCTS_CARTS_URL + "/id/delete")
                .with(user("John").password("password").roles("USER"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Product from cart deleted")
    void product_from_cart_deleted() throws Exception {
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

        when(cartService.findByUser(2))
            .thenReturn(new Cart(1, 0));

        mockMvc.perform(post(PRODUCTS_CARTS_URL + "/1/delete")
                .with(user("John").password("password").roles("USER"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Buy products in cart")
    void buy_products_in_cart() throws Exception {
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

        when(cartService.findByUser(2))
            .thenReturn(new Cart(1, 0));

        when(productsCartsService.findAllProductsInCart(1))
            .thenReturn(
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

        mockMvc.perform(post(PRODUCTS_CARTS_URL + "/buy")
                .with(user("John").password("password").roles("USER"))
                .with(csrf()))
            .andExpect(status().isOk());

        verify(productsCartsService).buy(2);
    }

    @Test
    @DisplayName("Download report")
    void download_report() throws Exception {
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

        when(walletService.findByUser(2))
            .thenReturn(new Wallet(1, "123", 0));

        mockMvc.perform(post(PRODUCTS_CARTS_URL + "/download-report")
                .with(user("John").password("password").roles("USER"))
                .with(csrf()))
            .andExpect(status().isOk());
    }
}
