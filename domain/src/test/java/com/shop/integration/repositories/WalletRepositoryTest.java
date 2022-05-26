package com.shop.integration.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.PersonDao;
import com.shop.dao.WalletDao;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    WalletRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/wallet/V20220421162043__Create_table_wallet.sql"
})
public class WalletRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(namedParameterJdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE wallet");
    }

    @Test
    @DisplayName("No wallets in database")
    void no_history_records_in_db() {
        int walletsCount = walletRepository.count();

        assertThat(walletsCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing wallet")
    void nothing_happened_when_trying_to_delete_not_existing_wallet() {
        assertThatCode(() -> walletRepository.delete(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Wallet was deleted")
    @DirtiesContext
    void wallet_was_deleted() {
        var walletToSave = Wallet.of("123", 0);

        walletRepository.save(walletToSave, 1);

        assertThat(walletRepository.count()).isEqualTo(1);

        walletRepository.delete(walletRepository.count());

        assertThat(walletRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save wallet and check wallet data")
    @DirtiesContext
    void save_wallet_and_check_wallet_data() {
        var walletToSave = Wallet.of("123", 0);
        walletRepository.save(walletToSave, 1);
        var savedWallet = walletRepository.findById(walletRepository.count());
        Wallet wallet = null;
        if (savedWallet.isPresent()) {
            wallet = savedWallet.get();
        }

        assertThat(wallet).extracting(Wallet::getId).isEqualTo(1L);
        assertThat(wallet).extracting(Wallet::getNumber).isEqualTo("123");
        assertThat(wallet).extracting(Wallet::getAmountOfMoney).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Save multiple wallets")
    @DirtiesContext
    void save_multiple_wallets() {
        walletRepository.save(Wallet.of("123", 0), 1);
        walletRepository.save(Wallet.of("456", 0), 2);

        assertThat(walletRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Wallet was not found")
    void wallet_was_not_found() {
        Optional<Wallet> wallet = walletRepository.findById(1);

        assertThat(wallet).isEmpty();
    }

    @Test
    @DisplayName("Wallet was found")
    @DirtiesContext
    void wallet_was_found() {
        walletRepository.save(Wallet.of("123", 0), 1);

        Optional<Wallet> wallet = walletRepository.findById(walletRepository.count());

        assertThat(wallet).get().isEqualTo(Wallet.of("123", 0).withId(1));
    }

    @Test
    @DisplayName("Find all wallets")
    @DirtiesContext
    void find_all_wallets() {
        walletRepository.save(Wallet.of("123", 0), 1);
        walletRepository.save(Wallet.of("456", 0), 2);

        var wallets = walletRepository.findAll();

        assertThat(wallets).isEqualTo(
            List.of(
                Wallet.of("123", 0).withId(1),
                Wallet.of("456", 0).withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public WalletDao walletDao() {
            return new WalletDao(namedParameterJdbcTemplate);
        }

        @Bean
        public WalletRepository walletRepository(WalletDao walletDao) {
            return new WalletRepository(walletDao);
        }
    }
}
