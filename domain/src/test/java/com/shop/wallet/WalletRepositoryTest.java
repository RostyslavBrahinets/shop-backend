package com.shop.wallet;

import com.shop.configs.DatabaseConfig;
import com.shop.admin_number.AdminNumberRepository;
import com.shop.user.UserRepository;
import com.shop.wallet.WalletRepository;
import com.shop.wallet.Wallet;
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
    "classpath:db/migration/admin_number/V20220421160504__Create_table_admin_number.sql",
    "classpath:db/migration/user/V20220421161642__Create_table_user.sql",
    "classpath:db/migration/wallet/V20220421162043__Create_table_wallet.sql"
})
public class WalletRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        AdminNumberRepository adminNumberRepository = new AdminNumberRepository(jdbcTemplate);
        adminNumberRepository.save("0");

        UserRepository userRepository = new UserRepository(jdbcTemplate);

        String firstName = "John";
        String lastName = "Smith";
        String email1 = "test1@email.com";
        String phone1 = "+380000000001";
        String email2 = "test2@email.com";
        String phone2 = "+380000000002";
        String password = "password";
        long adminNumberId = 1;

        userRepository.save(
            firstName,
            lastName,
            email1,
            phone1,
            password,
            adminNumberId
        );

        userRepository.save(
            firstName,
            lastName,
            email2,
            phone2,
            password,
            adminNumberId
        );

        walletRepository = new WalletRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "wallet", "\"user\"", "admin_number"
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
        walletRepository.save("123", 0, 1);

        var walletsCount = fetchWalletsCount();

        assertThat(walletsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple wallets")
    void save_multiple_wallets() {
        walletRepository.save("123", 0, 1);
        walletRepository.save("456", 0, 2);

        var walletsCount = fetchWalletsCount();

        assertThat(walletsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Wallet by id was not found")
    void wallet_by_id_was_not_found() {
        Optional<Wallet> wallet = walletRepository.findById(1);

        assertThat(wallet).isEmpty();
    }

    @Test
    @DisplayName("Wallet by id was found")
    void wallet_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("user_id", 1)
                )
            );

        Optional<Wallet> wallet = walletRepository.findById(1);

        assertThat(wallet).get().isEqualTo(Wallet.of("123", 0).withId(1));
    }

    @Test
    @DisplayName("Wallet by user was not found")
    void wallet_by_user_was_not_found() {
        Optional<Wallet> wallet = walletRepository.findByUser(1);

        assertThat(wallet).isEmpty();
    }

    @Test
    @DisplayName("Wallet by user was found")
    void wallet_by_user_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("user_id", 1)
                )
            );

        Optional<Wallet> wallet = walletRepository.findByUser(1);

        assertThat(wallet).get().isEqualTo(Wallet.of("123", 0).withId(1));
    }

    @Test
    @DisplayName("Find all wallets")
    void find_all_wallets() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("number", "123"),
                Map.entry("amount_of_money", 0),
                Map.entry("user_id", 1)
            ),
            Map.ofEntries(
                Map.entry("number", "456"),
                Map.entry("amount_of_money", 0),
                Map.entry("user_id", 2)
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "user_id")
            .executeBatch(batchInsertParameters);

        List<Wallet> wallets = walletRepository.findAll();

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
        assertThatCode(() -> walletRepository.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Wallet deleted")
    void wallet_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("user_id", 1)
                )
            );

        var walletsCountBeforeDeletion = fetchWalletsCount();

        assertThat(walletsCountBeforeDeletion).isEqualTo(1);

        walletRepository.delete(1);

        var walletsCount = fetchWalletsCount();

        assertThat(walletsCount).isZero();
    }

    @Test
    @DisplayName("Wallet updated")
    void wallet_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("wallet")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number", "amount_of_money", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("number", "123"),
                    Map.entry("amount_of_money", 0),
                    Map.entry("user_id", 1)
                )
            );

        walletRepository.update(1, 100);

        var updatedWallet = jdbcTemplate.queryForObject(
            "SELECT amount_of_money FROM wallet WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            Double.class
        );

        assertThat(updatedWallet).isEqualTo(100);
    }
}
