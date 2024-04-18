package com.shop.user;

import com.shop.adminnumber.AdminNumberRepository;
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
import java.util.Optional;

import static com.shop.SqlMigrationClasspath.ADMIN_NUMBER;
import static com.shop.SqlMigrationClasspath.USER;
import static com.shop.adminnumber.AdminNumberParameter.*;
import static com.shop.user.UserParameter.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    ADMIN_NUMBER,
    USER
})
class UserRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(jdbcTemplate);

        AdminNumberRepository adminNumberRepository = new AdminNumberRepository(jdbcTemplate);
        adminNumberRepository.save(getAdminNumberWithoutId());
        adminNumberRepository.save(getAdminNumberWithoutId2());
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "admin_number, \"user\""
        );
    }

    private int fetchUsersCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "\"user\""
        );
    }

    @Test
    @DisplayName("Find all users")
    void find_all_users() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            getMapOfEntries(getEmail(), getPhone(), getNumber()),
            getMapOfEntries(getEmail2(), getPhone2(), getNumber2())
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("\"user\"")
            .usingGeneratedKeyColumns("id")
            .usingColumns(
                "first_name",
                "last_name",
                "email",
                "phone",
                "password",
                "admin_number"
            )
            .executeBatch(batchInsertParameters);

        List<User> users = userRepository.findAll();

        assertThat(users).isEqualTo(
            List.of(
                getUserWithId(),
                getUserWithId2()
            )
        );
    }

    @Test
    @DisplayName("User by id was not found")
    void user_by_id_was_not_found() {
        Optional<User> user = userRepository.findById(getUserId());

        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("User by id was found")
    void user_by_id_was_found() {
        insertTestDataToDb();

        Optional<User> user = userRepository.findById(getUserId());

        assertThat(user).get().isEqualTo(getUserWithId());
    }

    @Test
    @DisplayName("Save user")
    void save_user() {
        userRepository.save(getUserWithoutId());

        var usersCount = fetchUsersCount();

        assertThat(usersCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple users")
    void save_multiple_users() {
        userRepository.save(getUserWithoutId());
        userRepository.save(getUserWithoutId2());

        var usersCount = fetchUsersCount();

        assertThat(usersCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Update user")
    void update_user() {
        insertTestDataToDb();

        Optional<User> adminNumber = userRepository.update(getUserId(), getUpdatedUserWithoutId());

        assertThat(adminNumber).get().isEqualTo(getUpdatedUserWithId());
    }

    @Test
    @DisplayName("User not deleted in case when not exists")
    void user_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> userRepository.delete(getUserWithId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete user")
    void delete_user() {
        insertTestDataToDb();

        var usersCountBeforeDeletion = fetchUsersCount();

        assertThat(usersCountBeforeDeletion).isEqualTo(1);

        userRepository.delete(getUserWithId());

        var usersCount = fetchUsersCount();

        assertThat(usersCount).isZero();
    }

    @Test
    @DisplayName("User by email was not found")
    void user_by_email_was_not_found() {
        Optional<User> user = userRepository.findByEmail(getEmail());

        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("User by email was found")
    void user_by_email_was_found() {
        insertTestDataToDb();

        Optional<User> user = userRepository.findByEmail(getEmail());

        assertThat(user).get().isEqualTo(getUserWithId());
    }

    private void insertTestDataToDb() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("\"user\"")
            .usingGeneratedKeyColumns("id")
            .usingColumns(
                "first_name",
                "last_name",
                "email",
                "phone",
                "password",
                "admin_number"
            )
            .execute(getMapOfEntries(getEmail(), getPhone(), getNumber()));
    }
}
