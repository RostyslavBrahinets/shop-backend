package com.shop.user;

import com.shop.adminnumber.AdminNumber;
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
import java.util.Map;
import java.util.Optional;

import static com.shop.SqlMigrationClasspath.*;
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
        adminNumberRepository.save(AdminNumber.of("12345678"));
        adminNumberRepository.save(AdminNumber.of("87654321"));
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
            Map.ofEntries(
                Map.entry("first_name", "John"),
                Map.entry("last_name", "Smith"),
                Map.entry("email", "test1@email.com"),
                Map.entry("phone", "+380000000001"),
                Map.entry("password", "password"),
                Map.entry("admin_number", "12345678")
            ),
            Map.ofEntries(
                Map.entry("first_name", "John"),
                Map.entry("last_name", "Smith"),
                Map.entry("email", "test2@email.com"),
                Map.entry("phone", "+380000000002"),
                Map.entry("password", "password"),
                Map.entry("admin_number", "87654321")
            )
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
                User.of(
                    "John",
                    "Smith",
                    "test1@email.com",
                    "+380000000001",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                ).withId(1),
                User.of(
                    "John",
                    "Smith",
                    "test2@email.com",
                    "+380000000002",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "87654321"
                ).withId(2)
            )
        );
    }

    @Test
    @DisplayName("User by id was not found")
    void user_by_id_was_not_found() {
        Optional<User> user = userRepository.findById(1);

        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("User by id was found")
    void user_by_id_was_found() {
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
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith"),
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("admin_number", "12345678")
                )
            );

        Optional<User> user = userRepository.findById(1);

        assertThat(user).get().isEqualTo(
            User.of(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "12345678"
            ).withId(1));
    }

    @Test
    @DisplayName("Save user")
    void save_user() {
        userRepository.save(
            User.of(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "12345678"
            )
        );

        var usersCount = fetchUsersCount();

        assertThat(usersCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple users")
    void save_multiple_users() {
        userRepository.save(
            User.of(
                "John",
                "Smith",
                "test1@email.com",
                "+380000000001",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "12345678"
            )
        );

        userRepository.save(
            User.of(
                "John",
                "Smith",
                "test2@email.com",
                "+380000000002",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "87654321"
            )
        );

        var usersCount = fetchUsersCount();

        assertThat(usersCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Update user")
    void update_user() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("first_name", "John"),
                Map.entry("last_name", "Smith"),
                Map.entry("email", "test@email.com"),
                Map.entry("phone", "+380000000000"),
                Map.entry("password", "password"),
                Map.entry("admin_number", "12345678")
            )
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

        Optional<User> adminNumber = userRepository.update(1, User.of("Alex", "Smith"));

        assertThat(adminNumber).get().isEqualTo(User.of(
            "Alex",
            "Smith",
            "test@email.com",
            "+380000000000",
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            "12345678"
        ).withId(1));
    }

    @Test
    @DisplayName("User not deleted in case when not exists")
    void user_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> userRepository.delete(User.of(null, null).withId(1))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete user")
    void delete_user() {
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
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith"),
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("admin_number", "12345678")
                )
            );

        var usersCountBeforeDeletion = fetchUsersCount();

        assertThat(usersCountBeforeDeletion).isEqualTo(1);

        userRepository.delete(User.of(null, null).withId(1));

        var usersCount = fetchUsersCount();

        assertThat(usersCount).isZero();
    }

    @Test
    @DisplayName("User by email was not found")
    void user_by_email_was_not_found() {
        Optional<User> user = userRepository.findByEmail("test@email.com");

        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("User by email was found")
    void user_by_email_was_found() {
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
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith"),
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("admin_number", "12345678")
                )
            );

        Optional<User> user = userRepository.findByEmail("test@email.com");

        assertThat(user).get().isEqualTo(
            User.of(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "12345678"
            ).withId(1));
    }
}
