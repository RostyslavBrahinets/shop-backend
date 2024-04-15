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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.shop.adminnumber.AdminNumberParameter.*;
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
            getMapOfEntries(getNumber()),
            getMapOfEntries(getNumber2())
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .executeBatch(batchInsertParameters);

        List<AdminNumber> adminNumbers = adminNumberRepository.findAll();

        assertThat(adminNumbers).isEqualTo(
            List.of(
                getAdminNumberWithId(),
                getAdminNumberWithId2()
            )
        );
    }

    @Test
    @DisplayName("Number of admin by id was not found")
    void number_of_admin_by_id_was_not_found() {
        Optional<AdminNumber> adminNumber = adminNumberRepository.findById(getAdminNumberId());

        assertThat(adminNumber).isEmpty();
    }

    @Test
    @DisplayName("Number of admin by id was found")
    void number_of_admin_by_id_was_found() {
        insertTestDataToDb();

        Optional<AdminNumber> adminNumber = adminNumberRepository.findById(getAdminNumberId());

        assertThat(adminNumber).get().isEqualTo(getAdminNumberWithId());
    }

    @Test
    @DisplayName("Save number of admin")
    void save_number_of_admin() {
        adminNumberRepository.save(getAdminNumberWithoutId());

        var adminNumbersCount = fetchCategoriesCount();

        assertThat(adminNumbersCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple numbers of admins")
    void save_multiple_numbers_of_admins() {
        adminNumberRepository.save(getAdminNumberWithoutId());
        adminNumberRepository.save(getAdminNumberWithoutId2());

        var adminNumbersCount = fetchCategoriesCount();

        assertThat(adminNumbersCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Update number of admin")
    void update_number_of_admin() {
        insertTestDataToDb();

        Optional<AdminNumber> adminNumber = adminNumberRepository.update(getAdminNumberId(), getAdminNumberWithoutId2());

        assertThat(adminNumber).get().isEqualTo(getAdminNumberWithId2(getAdminNumberId()));
    }

    @Test
    @DisplayName("Number of admin not deleted in case when not exists")
    void number_of_admin_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> adminNumberRepository.delete(getAdminNumberWithoutId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete number of admin")
    void delete_number_of_admin() {
        insertTestDataToDb();

        var adminNumbersCountBeforeDeletion = fetchCategoriesCount();

        assertThat(adminNumbersCountBeforeDeletion).isEqualTo(1);

        adminNumberRepository.delete(getAdminNumberWithoutId());

        var adminNumbersCount = fetchCategoriesCount();

        assertThat(adminNumbersCount).isZero();
    }

    @Test
    @DisplayName("Number of admin by number was not found")
    void number_of_admin_by_number_was_not_found() {
        Optional<AdminNumber> adminNumber = adminNumberRepository.findByNumber(getNumber());

        assertThat(adminNumber).isEmpty();
    }

    @Test
    @DisplayName("Number of admin by number was found")
    void number_of_admin_by_number_was_found() {
        insertTestDataToDb();

        Optional<AdminNumber> adminNumber = adminNumberRepository.findByNumber(getNumber());

        assertThat(adminNumber).get().isEqualTo(getAdminNumberWithId());
    }

    private void insertTestDataToDb() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("admin_number")
            .usingGeneratedKeyColumns("id")
            .usingColumns("number")
            .execute(Map.ofEntries(Map.entry("number", getNumber())));
    }

    private static Map<String, Serializable> getMapOfEntries(String number) {
        return Map.ofEntries(Map.entry("number", number));
    }
}
