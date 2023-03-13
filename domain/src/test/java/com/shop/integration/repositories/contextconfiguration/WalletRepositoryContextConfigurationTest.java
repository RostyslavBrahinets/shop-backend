package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.WalletDao;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
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
        WalletRepository.class,
        WalletRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class WalletRepositoryContextConfigurationTest {
    @Autowired
    private WalletDao walletDao;
    @Autowired
    private WalletRepository walletRepository;

    @Test
    @DisplayName("Get all wallets")
    void get_all_wallets() {
        walletRepository.findAll();

        verify(walletDao, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get wallet by id")
    void get_wallet_by_id() {
        long id = 1;

        walletRepository.findById(id);

        verify(walletDao).findById(id);
    }

    @Test
    @DisplayName("Get wallet by user")
    void get_wallet_by_user() {
        long userId = 1;

        walletRepository.findByUser(userId);

        verify(walletDao).findByUser(userId);
    }

    @Test
    @DisplayName("Save wallet")
    void save_wallet() {
        long userId = 1;
        String number = "123";
        double amountOfMoney = 0;

        walletRepository.save(number, amountOfMoney, userId);

        verify(walletDao).save(number, amountOfMoney, userId);
    }

    @Test
    @DisplayName("Update wallet")
    void update_wallet() {
        long id = 1;
        double amountOfMoney = 100;

        walletRepository.update(id, amountOfMoney);

        verify(walletDao).update(id, amountOfMoney);
    }

    @Test
    @DisplayName("Delete wallet")
    void delete_wallet() {
        long id = 1;

        walletRepository.delete(id);

        verify(walletDao).delete(id);
    }

    @Test
    @DisplayName("Count wallets")
    void count_wallets() {
        walletRepository.count();

        verify(walletDao, atLeast(1)).findAll();
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Wallet wallet() {
            return mock(Wallet.class);
        }

        @Bean
        public WalletDao walletDao() {
            return mock(WalletDao.class);
        }
    }
}
