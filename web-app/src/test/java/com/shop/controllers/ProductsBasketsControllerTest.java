package com.shop.controllers;

import com.shop.dto.ReportDto;
import com.shop.models.Basket;
import com.shop.models.Person;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.security.LoginPasswordAuthenticationProvider;
import com.shop.services.BasketService;
import com.shop.services.PersonService;
import com.shop.services.ProductsBasketsService;
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

import static com.shop.controllers.ProductsBasketsController.PRODUCTS_BASKETS_URL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(LoginPasswordAuthenticationProvider.class)
})
@WebMvcTest(ProductsBasketsController.class)
class ProductsBasketsControllerTest {
    @Autowired
    @MockBean
    private ProductsBasketsService productsBasketsService;
    @Autowired
    @MockBean
    private BasketService basketService;
    @Autowired
    @MockBean
    private PersonService personService;
    @Autowired
    @MockBean
    private WalletService walletService;
    @Autowired
    @MockBean
    private ReportDto report;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All products from basket request")
    void all_products_from_basket_request() throws Exception {
        when(personService.findByEmail("admin"))
            .thenReturn(new Person(1, "John", "Smith"));

        when(basketService.findByPerson(1))
            .thenReturn(new Basket(1, 0));

        when(productsBasketsService.findAllProductsInBasket(1))
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

        mockMvc.perform(get(PRODUCTS_BASKETS_URL)
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Save product to basket")
    void save_product_to_basket() throws Exception {
        when(personService.findByEmail("admin"))
            .thenReturn(new Person(1, "John", "Smith"));

        when(basketService.findByPerson(1))
            .thenReturn(new Basket(1, 0));

        when(productsBasketsService.saveProductToBasket(1, 1))
            .thenReturn(1L);

        mockMvc.perform(post(PRODUCTS_BASKETS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/basket"));

        verify(productsBasketsService).saveProductToBasket(1, 1);
    }


    @Test
    @DisplayName("Product from basket not deleted because of incorrect id")
    void product_from_basket_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(post(PRODUCTS_BASKETS_URL + "/id/delete")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Product from basket deleted")
    void product_from_basket_deleted() throws Exception {
        when(personService.findByEmail("admin"))
            .thenReturn(new Person(1, "John", "Smith"));

        when(basketService.findByPerson(1))
            .thenReturn(new Basket(1, 0));

        mockMvc.perform(post(PRODUCTS_BASKETS_URL + "/1/delete")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/basket"));
    }

    @Test
    @DisplayName("Buy products in basket")
    void buy_products_in_basket() throws Exception {
        when(personService.findByEmail("admin"))
            .thenReturn(new Person(1, "John", "Smith"));

        when(basketService.findByPerson(1))
            .thenReturn(new Basket(1, 0));

        when(productsBasketsService.findAllProductsInBasket(1))
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

        mockMvc.perform(post(PRODUCTS_BASKETS_URL + "/buy")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/basket"));

        verify(productsBasketsService).buy(1);
    }

    @Test
    @DisplayName("Download report")
    void download_report() throws Exception {
        when(personService.findByEmail("admin"))
            .thenReturn(new Person(1, "John", "Smith"));

        when(walletService.findByPerson(1))
            .thenReturn(new Wallet(1, "123", 0));

        mockMvc.perform(post(PRODUCTS_BASKETS_URL + "/download-report")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().isOk());
    }
}
