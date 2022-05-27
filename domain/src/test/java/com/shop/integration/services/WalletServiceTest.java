package com.shop.integration.services;

import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.services.WalletService;
import com.shop.validators.PersonValidator;
import com.shop.validators.WalletValidator;
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
    private PersonValidator personValidator;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletService = new WalletService(
            walletRepository,
            walletValidator,
            personValidator
        );
    }

    @Test
    @DisplayName("Wallet was saved for with correct input")
    void wallet_was_saved_with_correct_input() {
        when(walletRepository.save("123", 0, 1))
            .thenReturn(Wallet.of("123", 0).withId(1));

        Wallet savedWallet = walletService.save("123", 0, 1);

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
    @DisplayName("Wallet was found by person")
    void wallet_was_found_by_person() {
        when(walletRepository.findByPerson(1)).thenReturn(
            Optional.of(Wallet.of("123", 0).withId(1))
        );

        Wallet wallet = walletService.findByPerson(1);

        assertThat(wallet).isEqualTo(new Wallet(1, "123", 0));
    }

    @Test
    @DisplayName("Wallet was deleted")
    void wallet_was_deleted() {
        walletService.delete(1);
        verify(walletRepository).delete(1);
    }
}
