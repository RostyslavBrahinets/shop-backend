package com.shop.unit.services;

import com.shop.models.User;
import com.shop.repositories.UserRepository;
import com.shop.services.AdminNumberService;
import com.shop.services.UserService;
import com.shop.validators.AdminNumberValidator;
import com.shop.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private AdminNumberValidator adminNumberValidator;
    @Mock
    private AdminNumberService adminNumberService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userService = new UserService(
            userRepository,
            userValidator,
            adminNumberValidator,
            adminNumberService,
            passwordEncoder
        );
    }

    @Test
    @DisplayName("Empty list of users is returned in case when no users in storage")
    void empty_list_of_users_is_returned_in_case_when_no_users_in_storage() {
        when(userRepository.findAll()).thenReturn(emptyList());

        List<User> users = userService.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("List of users is returned in case when users are exists in storage")
    void list_of_users_is_returned_in_case_when_users_are_exists_in_storage() {
        when(userRepository.findAll()).thenReturn(
            List.of(
                User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    1
                ).withId(1)
            )
        );

        List<User> users = userService.findAll();

        assertThat(users).isEqualTo(
            List.of(
                new User(
                    1,
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    1
                )
            )
        );
    }

    @Test
    @DisplayName("User was found by id")
    void user_was_found_by_id() {
        when(userRepository.findById(1)).thenReturn(
            Optional.of(
                User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    1
                ).withId(1)
            )
        );

        User user = userService.findById(1);

        assertThat(user).isEqualTo(
            new User(1,
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                "password",
                1
            )
        );
    }

    @Test
    @DisplayName("User was found by email")
    void user_was_found_by_email() {
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(
            Optional.of(
                User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    "password",
                    1
                ).withId(1)
            )
        );

        User user = userService.findByEmail("test@gmail.com");

        assertThat(user).isEqualTo(
            new User(
                1,
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                "password",
                1
            )
        );
    }

    @Test
    @DisplayName("User was deleted")
    void user_was_deleted() {
        userService.delete(1);
        verify(userRepository).delete(1);
    }
}
