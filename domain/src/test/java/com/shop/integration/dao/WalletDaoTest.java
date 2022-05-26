package com.shop.integration.dao;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.PersonDao;
import com.shop.dao.WalletDao;
import com.shop.models.Wallet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/wallet/V20220421162043__Create_table_wallet.sql"
})
public class WalletDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private WalletDao walletDao;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(jdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        walletDao = new WalletDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "wallet", "person"
        );
    }

    private int fetchWalletsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "wallet"
        );
    }

    @Test
    @DisplayName("Save wallet")
    void save_wallet() {
        walletDao.save("123", 0, 1);

        var walletsCount = fetchWalletsCount();

        assertThat(walletsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple wallets")
    void save_multiple_wallets() {
        walletDao.save("123", 0, 1);
        walletDao.save("456", 0, 2);

        var walletsCount = fetchWalletsCount();

        assertThat(walletsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Wallet by id was not found")
    void wallet_by_id_was_not_found() {
        Optional<Wallet> wallet = walletDao.findById(1);

        assertThat(wallet).isEmpty();
    }

    @Test
    @DisplayName("Wallet by id was found")
    void wallet_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("person_id", 1)
                )
            );

        Optional<Wallet> wallet = walletDao.findById(1);

        assertThat(wallet).get().isEqualTo(Wallet.of("123", 0).withId(1));
    }

    @Test
    @DisplayName("Wallet by person was not found")
    void wallet_by_person_was_not_found() {
        Optional<Wallet> wallet = walletDao.findByPerson(1);

        assertThat(wallet).isEmpty();
    }

    @Test
    @DisplayName("Wallet by person was found")
    void wallet_by_person_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("person_id", 1)
                )
            );

        Optional<Wallet> wallet = walletDao.findByPerson(1);

        assertThat(wallet).get().isEqualTo(Wallet.of("123", 0).withId(1));
    }

    @Test
    @DisplayName("Find all wallets")
    void find_all_wallets() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("number", "123"),
                Map.entry("amount_of_money", 0),
                Map.entry("person_id", 1)
            ),
            Map.ofEntries(
                Map.entry("number", "456"),
                Map.entry("amount_of_money", 0),
                Map.entry("person_id", 2)
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "person_id")
            .executeBatch(batchInsertParameters);

        List<Wallet> wallets = walletDao.findAll();

        assertThat(wallets).isEqualTo(
            List.of(
                Wallet.of("123", 0).withId(1),
                Wallet.of("456", 0).withId(2)
            )
        );
    }

    @Test
    @DisplayName("Wallet not deleted in case when not exists")
    void wallet_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> walletDao.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Wallet deleted")
    void wallet_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("person_id", 1)
                )
            );

        var walletsCountBeforeDeletion = fetchWalletsCount();

        assertThat(walletsCountBeforeDeletion).isEqualTo(1);

        walletDao.delete(1);

        var walletsCount = fetchWalletsCount();

        assertThat(walletsCount).isZero();
    }

    @Test
    @DisplayName("Wallet updated")
    void wallet_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("person_id", 1)
                )
            );

        walletDao.update(1, 100);

        var updatedWallet = jdbcTemplate.queryForObject(
            "SELECT amount_of_money FROM wallet WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            Double.class
        );

        assertThat(updatedWallet).isEqualTo(100);
    }
}
