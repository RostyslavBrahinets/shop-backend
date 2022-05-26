package com.shop.integration.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.RoleDao;
import com.shop.models.Role;
import com.shop.repositories.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    RoleRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/role/V20220505172953__Create_table_role.sql",
    "classpath:db/migration/role/V20220505173022__Insert_data_to_table_role.sql"
})
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Role was not found")
    @DirtiesContext
    void role_was_not_found() {
        Optional<Role> role = roleRepository.findByName("role");

        assertThat(role).isEmpty();
    }

    @Test
    @DisplayName("Role was found")
    @DirtiesContext
    void role_was_found() {
        Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");

        assertThat(role).get().isEqualTo(Role.of("ROLE_ADMIN").withId(1));
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public RoleDao roleDao() {
            return new RoleDao(namedParameterJdbcTemplate);
        }

        @Bean
        public RoleRepository roleRepository(RoleDao roleDao) {
            return new RoleRepository(roleDao);
        }
    }
}
