package com.shop.user;

import com.shop.adminnumber.AdminNumber;
import com.shop.adminnumber.AdminNumberService;
import com.shop.adminnumber.AdminNumberValidator;
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

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        UserService.class,
        UserServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class UserServiceContextConfigurationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AdminNumberService adminNumberService;
    @Autowired
    private UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = List.of();

        adminNumberService.save(AdminNumber.of("12345678"));
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
        long id = 1;

        userService.findById(id);

        verify(userValidator, atLeast(1)).validate(id, users);
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("Get user by email")
    void get_user_by_email() {
        String email = "test@email.com";

        userService.findByEmail(email);

        verify(userValidator, atLeast(1)).validateEmail(email);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Update user")
    void update_user() {
        long id = 1;
        String firstName = "John";
        String lastName = "Smith";

        userService.update(User.of(firstName, lastName).withId(id));

        verify(userValidator, atLeast(1)).validate(id, users);
        verify(userValidator, atLeast(1)).validateFullName(firstName, lastName);
        verify(userRepository).update(User.of(firstName, lastName).withId(1));
    }

    @Test
    @DisplayName("Delete user")
    void delete_user() {
        long id = 1;

        userService.delete(User.of(null, null).withId(id));

        verify(userValidator, atLeast(1)).validate(id, users);
        verify(userRepository).delete(User.of(null, null).withId(1));
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
        public PasswordEncoder passwordEncoder() {
            return mock(PasswordEncoder.class);
        }
    }
}
