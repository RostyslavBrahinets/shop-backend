package com.shop.userrole;

import com.shop.configs.DatabaseConfig;
import com.shop.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import static com.shop.SqlMigrationClasspath.*;
import static com.shop.role.RoleParameter.*;
import static com.shop.user.UserParameter.getUserId;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    USER_ROLE,
    ROLE,
    INSERT_ROLE
})
class UserRoleRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private UserRoleRepository userRoleRepository;

    @BeforeEach
    void setUp() {
        userRoleRepository = new UserRoleRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "user_role", "role"
        );
    }

    private int fetchUserRoleCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "user_role"
        );
    }

    @Test
    @DisplayName("Save role for user")
    void save_role_for_user() {
        userRoleRepository.saveRoleForUser(getUserId(), getRoleId());

        var userRoleCount = fetchUserRoleCount();

        assertThat(userRoleCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Role for user was not found")
    void role_for_user_was_not_found() {
        Optional<Role> role = userRoleRepository.findRoleForUser(getUserId());

        assertThat(role).isEmpty();
    }

    @Test
    @DisplayName("Role for user was found")
    void role_for_user_was_found() {
        insertTestDataToDb();

        Optional<Role> role = userRoleRepository.findRoleForUser(getUserId());

        assertThat(role).get().isEqualTo(getRoleWithId("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("Role for user updated")
    void role_for_user_updated() {
        insertTestDataToDb();

        userRoleRepository.updateRoleForUser(getUserId(), getRoleId2());

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT role_id FROM user_role WHERE user_id=:user_id",
            Map.ofEntries(Map.entry("user_id", getUserId())),
            Integer.class
        );

        assertThat(updatedBasket).isEqualTo(2);
    }

    private static Map<String, Serializable> getMapOfEntries() {
        return Map.ofEntries(
            Map.entry("user_id", getUserId()),
            Map.entry("role_id", getRoleId())
        );
    }

    private void insertTestDataToDb() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("user_role")
            .usingColumns("user_id", "role_id")
            .execute(getMapOfEntries());
    }
}


