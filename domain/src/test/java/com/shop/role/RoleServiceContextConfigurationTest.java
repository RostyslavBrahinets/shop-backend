package com.shop.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.shop.role.RoleParameter.getName;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        RoleService.class,
        RoleServiceContextConfigurationTest.TestContextConfig.class
    }
)
class RoleServiceContextConfigurationTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;

    @Test
    @DisplayName("Get role by name")
    void get_basket_by_name() {
        roleService.findByName(getName());

        verify(roleRepository).findByName(getName());
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public RoleRepository roleRepository() {
            return mock(RoleRepository.class);
        }
    }
}
