package com.shop.services.contextconfiguration;

import com.shop.repositories.PersonRoleRepository;
import com.shop.services.PersonRoleService;
import com.shop.validators.PersonValidator;
import com.shop.validators.RoleValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        PersonRoleService.class,
        PersonRoleServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class PersonRoleServiceContextConfigurationTest {
    @Autowired
    private PersonRoleRepository personRoleRepository;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private RoleValidator roleValidator;
    @Autowired
    private PersonRoleService personRoleService;

    @Test
    @DisplayName("Get role by person")
    void get_role_by_person() {
        long personId = 1;

        personRoleService.findRoleByPerson(personId);

        verify(personValidator, atLeast(1)).validate(personId);
        verify(personRoleRepository).findRoleByPerson(personId);
    }

    @Test
    @DisplayName("Save role for person")
    void save_role_for_person() {
        long personId = 1;
        long roleId = 1;

        personRoleService.saveRoleForPerson(personId, roleId);

        verify(personValidator, atLeast(1)).validate(personId);
        verify(roleValidator, atLeast(1)).validate(roleId);
        verify(personRoleRepository).saveRoleForPerson(personId, roleId);
    }

    @Test
    @DisplayName("Update role for person")
    void update_role_for_person() {
        long personId = 1;
        long roleId = 1;

        personRoleService.updateRoleForPerson(personId, roleId);

        verify(personValidator, atLeast(1)).validate(personId);
        verify(roleValidator, atLeast(1)).validate(roleId);
        verify(personRoleRepository).updateRoleForPerson(personId, roleId);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public PersonRoleRepository personRoleRepository() {
            return mock(PersonRoleRepository.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }

        @Bean
        public RoleValidator roleValidator() {
            return mock(RoleValidator.class);
        }
    }
}
