package com.shop.wallet;

import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletValidator walletValidator;
    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletService = new WalletService(
            walletRepository,
            walletValidator,
            userService,
            userValidator
        );
    }

    @Test
    @DisplayName("Wallet was saved for with correct input")
    void wallet_was_saved_with_correct_input() {
        Wallet savedWallet = walletService.save(Wallet.of("123", 0, 1));

        verify(walletRepository).save("123", 0, 1);

        assertThat(savedWallet).isEqualTo(new Wallet(1, "123", 0));
    }

    @Test
    @DisplayName("Empty list of wallets is returned in case when no wallets in storage")
    void empty_list_of_wallets_is_returned_in_case_when_no_wallets_in_storage() {
        when(walletRepository.findAll()).thenReturn(emptyList());

        List<Wallet> wallets = walletService.findAll();

        assertThat(wallets).isEmpty();
    }

    @Test
    @DisplayName("List of wallets is returned in case when wallets are exists in storage")
    void list_of_wallets_is_returned_in_case_when_wallets_are_exists_in_storage() {
        when(walletRepository.findAll()).thenReturn(
            List.of(
                Wallet.of("123", 0).withId(1)
            )
        );

        List<Wallet> wallets = walletService.findAll();

        assertThat(wallets).isEqualTo(List.of(new Wallet(1, "123", 0)));
    }

    @Test
    @DisplayName("Wallet was found by id")
    void basket_was_found_by_id() {
        when(walletRepository.findById(1)).thenReturn(
            Optional.of(Wallet.of("123", 0).withId(1))
        );

        Wallet wallet = walletService.findById(1);

        assertThat(wallet).isEqualTo(new Wallet(1, "123", 0));
    }

    @Test
    @DisplayName("Wallet was found by user")
    void wallet_was_found_by_user() {
        when(walletRepository.findByUser(1)).thenReturn(
            Optional.of(Wallet.of("123", 0).withId(1))
        );

        Wallet wallet = walletService.findByUser(User.of(null, null).withId(1));

        assertThat(wallet).isEqualTo(new Wallet(1, "123", 0));
    }

    @Test
    @DisplayName("Wallet was deleted")
    void wallet_was_deleted() {
        walletService.delete(Wallet.of(null, 0, 0).withId(1));
        verify(walletRepository).delete(1);
    }
}
