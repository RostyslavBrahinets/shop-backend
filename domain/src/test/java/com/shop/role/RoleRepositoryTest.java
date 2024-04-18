package com.shop.role;

import com.shop.configs.DatabaseConfig;
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

import static com.shop.SqlMigrationClasspath.ROLE;
import static com.shop.role.RoleParameter.getName;
import static com.shop.role.RoleParameter.getRoleWithId;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    ROLE
})
class RoleRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository = new RoleRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "role"
        );
    }

    @Test
    @DisplayName("Role by name was not found")
    void role_by_name_was_not_found() {
        Optional<Role> role = roleRepository.findByName(getName());

        assertThat(role).isEmpty();
    }

    @Test
    @DisplayName("Role by name was found")
    void role_by_name_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("role")
            .usingGeneratedKeyColumns("id")
            .usingColumns("name")
            .execute(Map.ofEntries(Map.entry("name", getName())));

        Optional<Role> role = roleRepository.findByName(getName());

        assertThat(role).get().isEqualTo(getRoleWithId());
    }
}
