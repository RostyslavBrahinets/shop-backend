package com.shop.mvc;

import com.shop.exceptions.NotFoundException;
import com.shop.user.User;
import com.shop.wallet.Wallet;
import com.shop.security.SignInPasswordAuthenticationProvider;
import com.shop.user.UserService;
import com.shop.wallet.WalletService;
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
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(WalletViewController.class)
class WalletViewControllerTest {
    @MockBean
    @Autowired
    private UserService userService;
    @MockBean
    @Autowired
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Redirect guest from wallet to sign in")
    void redirect_guest_from_wallet_to_sign_in() throws Exception {
        mockMvc.perform(get("/wallet")
                .with(anonymous()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/sign-in"));
    }

    @Test
    @DisplayName("Show wallet for user")
    void show_wallet_for_user() throws Exception {
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

        when(walletService.findByUser(2)).thenReturn(
            new Wallet(1, "123", 0)
        );

        mockMvc.perform(get("/wallet")
                .with(user("test@email.com").password("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(view().name("wallet/index"))
            .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("Wallet not found for admin")
    void wallet_not_found_for_admin() throws Exception {
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

        when(walletService.findByUser(1)).thenThrow(
            new NotFoundException("Wallet Not Found")
        );

        mockMvc.perform(get("/wallet")
                .with(user("admin").password("admin").roles("ADMIN")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
