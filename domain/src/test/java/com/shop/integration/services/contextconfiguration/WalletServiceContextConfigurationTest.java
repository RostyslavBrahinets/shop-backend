package com.shop.integration.services.contextconfiguration;

import com.shop.models.User;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.services.UserService;
import com.shop.services.WalletService;
import com.shop.validators.UserValidator;
import com.shop.validators.WalletValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        WalletService.class,
        WalletServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class WalletServiceContextConfigurationTest {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletValidator walletValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private WalletService walletService;

    private List<Wallet> wallets;
    private List<User> users;

    @BeforeEach
    void setUp() {
        wallets = List.of();
        users = List.of();
    }

    @Test
    @DisplayName("Get all wallets")
    void get_all_wallets() {
        walletService.findAll();

        verify(walletRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get wallet by id")
    void get_wallet_by_id() {
        long id = 1;

        walletService.findById(id);

        verify(walletValidator, atLeast(1)).validate(id, wallets);
        verify(walletRepository, atLeast(1)).findById(id);
    }

    @Test
    @DisplayName("Get wallet by user")
    void get_wallet_by_user() {
        long userId = 1;

        walletService.findByUser(userId);

        verify(userValidator, atLeast(1)).validate(userId, users);
        verify(walletRepository).findByUser(1);
    }

    @Test
    @DisplayName("Save wallet")
    void save_wallet() {
        long userId = 1;
        String number = "123";
        double amountOfMoney = 0;

        walletService.save(number, amountOfMoney, userId);

        verify(walletValidator, atLeast(1)).validate(number, amountOfMoney);
        verify(userValidator, atLeast(1)).validate(userId, users);
        verify(walletRepository).save(number, amountOfMoney, userId);
    }

    @Test
    @DisplayName("Update wallet")
    void update_wallet() {
        long id = 1;
        double amountOfMoney = 100;

        walletService.update(id, amountOfMoney);

        verify(walletValidator, atLeast(1)).validate(id, wallets);
        verify(walletValidator, atLeast(1)).validateAmountOfMoney(amountOfMoney);
        verify(walletRepository).update(1, amountOfMoney);
    }

    @Test
    @DisplayName("Delete wallet")
    void delete_wallet() {
        long id = 1;

        walletService.delete(id);

        verify(walletValidator, atLeast(1)).validate(id, wallets);
        verify(walletRepository).delete(id);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Wallet wallet() {
            return mock(Wallet.class);
        }

        @Bean
        public WalletRepository walletRepository() {
            return mock(WalletRepository.class);
        }

        @Bean
        public WalletValidator walletValidator() {
            return mock(WalletValidator.class);
        }

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public UserValidator userValidator() {
            return mock(UserValidator.class);
        }
    }
}
