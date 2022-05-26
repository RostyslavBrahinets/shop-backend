package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.RoleDao;
import com.shop.repositories.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        RoleRepository.class,
        RoleRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class RoleRepositoryContextConfigurationTest {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Get role by name")
    void get_basket_by_person() {
        String name = "name";

        roleRepository.findByName(name);

        verify(roleDao).findByName(name);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public RoleDao roleDao() {
            return mock(RoleDao.class);
        }
    }
}
