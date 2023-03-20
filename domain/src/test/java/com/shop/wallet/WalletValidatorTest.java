package com.shop.wallet;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class WalletValidatorTest {
    private WalletValidator walletValidator;

    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletValidator = new WalletValidator();
    }

    @Test
    @DisplayName("Wallet validated without exceptions")
    void wallet_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> walletValidator.validate("123", 0)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because amount of money in wallet less then expected")
    void throw_validation_exception_because_amount_of_money_in_wallet_less_then_expected() {
        assertThrows(
            ValidationException.class,
            () -> walletValidator.validate("123", -1.0)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number of wallet is null")
    void throw_validation_exception_because_number_of_wallet_is_null() {
        assertThrows(
            ValidationException.class,
            () -> walletValidator.validate(null, 0)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because number of wallet is empty")
    void throw_validation_exception_because_number_of_wallet_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> walletValidator.validate("", 0)
        );
    }

    @Test
    @DisplayName("Amount of money validated without exceptions")
    void amount_of_money_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> walletValidator.validateAmountOfMoney(0)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because amount of money less then expected")
    void throw_validation_exception_because_amount_of_money_less_then_expected() {
        assertThrows(
            ValidationException.class,
            () -> walletValidator.validateAmountOfMoney(-1.0)
        );
    }

    @Test
    @DisplayName("Id of wallet validated without exceptions")
    void id_of_wallet_validated_without_exceptions() {
        List<Wallet> wallets = List.of(
            Wallet.of("123", 0, 0).withId(1)
        );

        when(walletRepository.findAll()).thenReturn(wallets);

        assertDoesNotThrow(
            () -> walletValidator.validate(1, wallets)
        );
    }

    @Test
    @DisplayName("Throw NotFoundException because id of wallet not found")
    void throw_not_found_exception_because_id_of_wallet_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> walletValidator.validate(1, List.of())
        );
    }
}
