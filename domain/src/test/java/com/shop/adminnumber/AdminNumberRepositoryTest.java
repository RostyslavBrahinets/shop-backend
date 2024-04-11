package com.shop.adminnumber;

import com.shop.configs.DatabaseConfig;
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
    "classpath:db/migration/adminnumber/V20220421160504__Create_table_admin_number.sql"
})
class AdminNumberRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private AdminNumberRepository adminNumberRepository;

    @BeforeEach
    void setUp() {
        adminNumberRepository = new AdminNumberRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "admin_number"
        );
    }

    private int fetchCategoriesCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "admin_number"
        );
    }

    @Test
    @DisplayName("Find all numbers of admins")
    void find_all_numbers_of_admins() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(Map.entry("number", "12345678")),
            Map.ofEntries(Map.entry("number", "87654321"))
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .executeBatch(batchInsertParameters);

        List<AdminNumber> adminNumbers = adminNumberRepository.findAll();

        assertThat(adminNumbers).isEqualTo(
            List.of(
                AdminNumber.of("12345678").withId(1),
                AdminNumber.of("87654321").withId(2)
            )
        );
    }

    @Test
    @DisplayName("Number of admin by id was not found")
    void number_of_admin_by_id_was_not_found() {
        Optional<AdminNumber> adminNumber = adminNumberRepository.findById(1);

        assertThat(adminNumber).isEmpty();
    }

    @Test
    @DisplayName("Number of admin by id was found")
    void number_of_admin_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .execute(Map.ofEntries(Map.entry("number", "12345678")));

        Optional<AdminNumber> adminNumber = adminNumberRepository.findById(1);

        assertThat(adminNumber).get().isEqualTo(AdminNumber.of("12345678").withId(1));
    }

    @Test
    @DisplayName("Save number of admin")
    void save_number_of_admin() {
        adminNumberRepository.save(AdminNumber.of("12345678"));

        var adminNumbersCount = fetchCategoriesCount();

        assertThat(adminNumbersCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple numbers of admins")
    void save_multiple_numbers_of_admins() {
        adminNumberRepository.save(AdminNumber.of("12345678"));
        adminNumberRepository.save(AdminNumber.of("87654321"));

        var adminNumbersCount = fetchCategoriesCount();

        assertThat(adminNumbersCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Find all numbers of admins")
    void find_all_numbers_of_admins() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(Map.entry("number", "12345678")),
            Map.ofEntries(Map.entry("number", "87654321"))
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .executeBatch(batchInsertParameters);

        List<AdminNumber> adminNumbers = adminNumberRepository.findAll();

        assertThat(adminNumbers).isEqualTo(
            List.of(
                AdminNumber.of("12345678").withId(1),
                AdminNumber.of("87654321").withId(2)
            )
        );
    }

    @Test
    @DisplayName("Number of admin not deleted in case when not exists")
    void number_of_admin_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> adminNumberRepository.delete(AdminNumber.of("12345678"))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete number of admin")
    void delete_number_of_admin() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .execute(Map.ofEntries(Map.entry("number", "12345678")));

        var adminNumbersCountBeforeDeletion = fetchCategoriesCount();

        assertThat(adminNumbersCountBeforeDeletion).isEqualTo(1);

        adminNumberRepository.delete(AdminNumber.of("12345678"));

        var adminNumbersCount = fetchCategoriesCount();

        assertThat(adminNumbersCount).isZero();
    }

    @Test
    @DisplayName("Number of admin by number was not found")
    void number_of_admin_by_number_was_not_found() {
        Optional<AdminNumber> adminNumber = adminNumberRepository.findByNumber("12345678");

        assertThat(adminNumber).isEmpty();
    }

    @Test
    @DisplayName("Number of admin by number was found")
    void number_of_admin_by_number_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .execute(Map.ofEntries(Map.entry("number", "12345678")));

        Optional<AdminNumber> adminNumber = adminNumberRepository.findByNumber("12345678");

        assertThat(adminNumber).get().isEqualTo(AdminNumber.of("12345678").withId(1));
    }
}
