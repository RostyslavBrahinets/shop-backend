package com.shop.integration.services.contextconfiguration;

import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.services.WalletService;
import com.shop.validators.PersonValidator;
import com.shop.validators.WalletValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    private PersonValidator personValidator;
    @Autowired
    private WalletService walletService;

    @Test
    @DisplayName("Get all wallets")
    void get_all_wallets() {
        walletService.findAll();

        verify(walletRepository).findAll();
    }

    @Test
    @DisplayName("Get wallet by id")
    void get_wallet_by_id() {
        long id = 1;

        walletService.findById(id);

        verify(walletValidator, atLeast(1)).validate(id);
        verify(walletRepository).findById(id);
    }

    @Test
    @DisplayName("Get wallet by person")
    void get_wallet_by_person() {
        long personId = 1;

        walletService.findByPerson(personId);

        verify(personValidator, atLeast(1)).validate(personId);
        verify(walletRepository).findByPerson(1);
    }

    @Test
    @DisplayName("Save wallet")
    void save_wallet() {
        long personId = 1;
        String number = "123";
        double amountOfMoney = 0;

        walletService.save(number, amountOfMoney, personId);

        verify(walletValidator, atLeast(1)).validate(number, amountOfMoney);
        verify(personValidator, atLeast(1)).validate(personId);
        verify(walletRepository).save(number, amountOfMoney, personId);
    }

    @Test
    @DisplayName("Update wallet")
    void update_wallet() {
        long id = 1;
        double amountOfMoney = 100;

        walletService.update(id, amountOfMoney);

        verify(walletValidator, atLeast(1)).validate(id);
        verify(walletValidator, atLeast(1)).validateAmountOfMoney(amountOfMoney);
        verify(walletRepository).update(1, amountOfMoney);
    }

    @Test
    @DisplayName("Delete wallet")
    void delete_wallet() {
        long id = 1;

        walletService.delete(id);

        verify(walletValidator, atLeast(1)).validate(id);
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
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }
    }
}
