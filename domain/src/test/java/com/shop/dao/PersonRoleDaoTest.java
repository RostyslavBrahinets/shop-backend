package com.shop.dao;

import com.shop.configs.DatabaseConfig;
import com.shop.models.Role;
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
    "classpath:db/migration/person_role/V20220505173243__Create_table_person_role.sql",
    "classpath:db/migration/role/V20220505172953__Create_table_role.sql",
    "classpath:db/migration/role/V20220505173022__Insert_data_to_table_role.sql"
})
public class PersonRoleDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private PersonRoleDao personRoleDao;

    @BeforeEach
    void setUp() {
        personRoleDao = new PersonRoleDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "person_role", "role"
        );
    }

    private int fetchPersonRoleCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "person_role"
        );
    }

    @Test
    @DisplayName("Save role for person")
    void save_role_for_person() {
        personRoleDao.saveRoleForPerson(1, 1);

        var personRoleCount = fetchPersonRoleCount();

        assertThat(personRoleCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Role for person was not found")
    void role_for_person_was_not_found() {
        Optional<Role> role = personRoleDao.findRoleByPerson(1);

        assertThat(role).isEmpty();
    }

    @Test
    @DisplayName("Role for person was found")
    void role_for_person_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person_role")
            .usingColumns("person_id", "role_id")
            .execute(
                Map.ofEntries(
                    Map.entry("person_id", 1),
                    Map.entry("role_id", 1)
                )
            );

        Optional<Role> role = personRoleDao.findRoleByPerson(1);

        assertThat(role).get().isEqualTo(Role.of("ROLE_ADMIN").withId(1));
    }

    @Test
    @DisplayName("Role for person updated")
    void role_for_person_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person_role")
            .usingColumns("person_id", "role_id")
            .execute(
                Map.ofEntries(
                    Map.entry("person_id", 1),
                    Map.entry("role_id", 1)
                )
            );

        personRoleDao.updateRoleForPerson(1, 2);

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT role_id FROM person_role WHERE person_id=:person_id",
            Map.ofEntries(Map.entry("person_id", 1)),
            Integer.class
        );

        assertThat(updatedBasket).isEqualTo(2);
    }
}


