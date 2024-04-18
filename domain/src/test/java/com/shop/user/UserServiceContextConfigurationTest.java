package com.shop.user;

import com.shop.adminnumber.AdminNumberService;
import com.shop.adminnumber.AdminNumberValidator;
import com.shop.cart.CartRepository;
import com.shop.userrole.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.shop.adminnumber.AdminNumberParameter.getAdminNumberWithoutId;
import static com.shop.user.UserParameter.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        UserService.class,
        UserServiceContextConfigurationTest.TestContextConfig.class
    }
)
class UserServiceContextConfigurationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AdminNumberService adminNumberService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        adminNumberService.save(getAdminNumberWithoutId());
    }

    @Test
    @DisplayName("Get all users")
    void get_all_users() {
        userService.findAll();

        verify(userRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get user by id")
    void get_user_by_id() {
        userService.findById(getUserId());

        verify(userValidator, atLeast(1)).validate(getUserId(), getUsers());
        verify(userRepository).findById(getUserId());
    }

    @Test
    @DisplayName("Get user by email")
    void get_user_by_email() {
        userService.findByEmail(getEmail());

        verify(userValidator, atLeast(1)).validateEmail(getEmail());
        verify(userRepository).findByEmail(getEmail());
    }

    @Test
    @DisplayName("Update user")
    void update_user() {
        userService.update(getUserId(), getUserWithoutId());

        verify(userValidator, atLeast(1)).validate(getUserId(), getUsers());
        verify(userValidator, atLeast(1)).validateFullName(getFirstName(), getLastName());
        verify(userRepository).update(getUserId(), getUserWithoutId());
    }

    @Test
    @DisplayName("Delete user")
    void delete_user() {
        userService.delete(getUserWithId());

        verify(userValidator, atLeast(1)).validate(getUserId(), getUsers());
        verify(userRepository).delete(getUserWithId());
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        public UserValidator userValidator() {
            return mock(UserValidator.class);
        }

        @Bean
        public AdminNumberValidator adminNumberValidator() {
            return mock(AdminNumberValidator.class);
        }

        @Bean
        public AdminNumberService adminNumberService() {
            return mock(AdminNumberService.class);
        }

        @Bean
        public CartRepository cartRepository() {
            return mock(CartRepository.class);
        }

        @Bean
        public UserRoleRepository userRoleRepository() {
            return mock(UserRoleRepository.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return mock(PasswordEncoder.class);
        }
    }
}
