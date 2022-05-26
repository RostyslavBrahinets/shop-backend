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
    private Wallet wallet;
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
    @DisplayName("Get wallet by person")
    void get_wallet_by_person() {
        long personId = 1;

        walletRepository.findByPerson(personId);

        verify(walletDao).findByPerson(personId);
    }

    @Test
    @DisplayName("Save wallet")
    void save_wallet() {
        long personId = 1;

        walletRepository.save(wallet, personId);

        verify(walletDao).save(
            wallet.getNumber(),
            wallet.getAmountOfMoney(),
            personId
        );
    }

    @Test
    @DisplayName("Update wallet")
    void update_wallet() {
        long id = 1;

        walletRepository.update(id, wallet);

        verify(walletDao).update(id, wallet.getAmountOfMoney());
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
