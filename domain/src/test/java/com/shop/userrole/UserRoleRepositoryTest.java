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

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/userrole/V20220505173244__Create_table_user_role.sql",
    "classpath:db/migration/role/V20220505172953__Create_table_role.sql",
    "classpath:db/migration/role/V20220505173022__Insert_data_to_table_role.sql"
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
        userRoleRepository.saveRoleForUser(1, 1);

        var userRoleCount = fetchUserRoleCount();

        assertThat(userRoleCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Role for user was not found")
    void role_for_user_was_not_found() {
        Optional<Role> role = userRoleRepository.findRoleForUser(1);

        assertThat(role).isEmpty();
    }

    @Test
    @DisplayName("Role for user was found")
    void role_for_user_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("user_role")
            .usingColumns("user_id", "role_id")
            .execute(
                Map.ofEntries(
                    Map.entry("user_id", 1),
                    Map.entry("role_id", 1)
                )
            );

        Optional<Role> role = userRoleRepository.findRoleForUser(1);

        assertThat(role).get().isEqualTo(Role.of("ROLE_ADMIN").withId(1));
    }

    @Test
    @DisplayName("Role for user updated")
    void role_for_user_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("user_role")
            .usingColumns("user_id", "role_id")
            .execute(
                Map.ofEntries(
                    Map.entry("user_id", 1),
                    Map.entry("role_id", 1)
                )
            );

        userRoleRepository.updateRoleForUser(1, 2);

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT role_id FROM user_role WHERE user_id=:user_id",
            Map.ofEntries(Map.entry("user_id", 1)),
            Integer.class
        );

        assertThat(updatedBasket).isEqualTo(2);
    }
}


