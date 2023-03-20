package com.shop.wallet;

import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.stripe.StripePayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.shop.wallet.WalletController.WALLETS_URL;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class),
    @MockBean(StripePayment.class)
})
@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired
    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("All wallets request")
    void all_wallets_request() throws Exception {
        when(walletService.findAll()).thenReturn(
            List.of(
                new Wallet(1, "123", 0)
            )
        );

        when(walletService.update(Wallet.of("123", 100, 0).withId(1))).thenReturn(
            new Wallet(1, "123", 100, 0)
        );

        mockMvc.perform(get(WALLETS_URL)
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Wallet not found because of incorrect id")
    void wallet_not_found_because_of_incorrect_id() throws Exception {
        mockMvc.perform(get(WALLETS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Wallet found")
    void wallet_found() throws Exception {
        when(walletService.findById(1))
            .thenReturn(new Wallet(1, "123", 0, 0));

        mockMvc.perform(get(WALLETS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Wallet not deleted because of incorrect id")
    void wallet_not_deleted_because_of_incorrect_id() throws Exception {
        mockMvc.perform(delete(WALLETS_URL + "/id")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Wallet deleted")
    void wallet_deleted() throws Exception {
        mockMvc.perform(delete(WALLETS_URL + "/1")
                .with(user("admin").password("admin").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is2xxSuccessful());
    }
}
