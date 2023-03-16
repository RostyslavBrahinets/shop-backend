package com.shop.userrole;

import com.shop.role.RoleValidator;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
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
        UserRoleService.class,
        UserRoleServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class UserRoleServiceContextConfigurationTest {
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleValidator roleValidator;
    @Autowired
    private UserRoleService userRoleService;

    @Test
    @DisplayName("Get role by user")
    void get_role_by_user() {
        long userId = 1;

        userRoleService.findRoleForUser(userId);

        verify(userValidator, atLeast(1)).validate(userId, userService.findAll());
        verify(userRoleRepository).findRoleForUser(userId);
    }

    @Test
    @DisplayName("Save role for user")
    void save_role_for_user() {
        long userId = 1;
        long roleId = 1;

        userRoleService.saveRoleForUser(userId, roleId);

        verify(userValidator, atLeast(1)).validate(userId, userService.findAll());
        verify(roleValidator, atLeast(1)).validate(roleId);
        verify(userRoleRepository).saveRoleForUser(userId, roleId);
    }

    @Test
    @DisplayName("Update role for user")
    void update_role_for_user() {
        long userId = 1;
        long roleId = 1;

        userRoleService.updateRoleForUser(userId, roleId);

        verify(userValidator, atLeast(1)).validate(userId, userService.findAll());
        verify(roleValidator, atLeast(1)).validate(roleId);
        verify(userRoleRepository).updateRoleForUser(userId, roleId);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public UserRoleRepository userRoleRepository() {
            return mock(UserRoleRepository.class);
        }

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public UserValidator userValidator() {
            return mock(UserValidator.class);
        }

        @Bean
        public RoleValidator roleValidator() {
            return mock(RoleValidator.class);
        }
    }
}
