package com.shop.user;

import com.shop.adminnumber.AdminNumberRepository;
import com.shop.adminnumber.AdminNumberValidator;
import com.shop.cart.CartRepository;
import com.shop.userrole.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.shop.adminnumber.AdminNumberParameter.getAdminNumbers;
import static com.shop.adminnumber.AdminNumberParameter.getNumber;
import static com.shop.cart.CartParameter.getCartWithId;
import static com.shop.user.UserParameter.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private AdminNumberValidator adminNumberValidator;
    @Mock
    private AdminNumberRepository AdminNumberRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
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
            AdminNumberRepository,
            cartRepository,
            userRoleRepository,
            passwordEncoder
        );
    }

    @Test
    @DisplayName("Empty list of users is returned in case when no users in storage")
    void empty_list_of_users_is_returned_in_case_when_no_users_in_storage() {
        when(userRepository.findAll()).thenReturn(emptyList());

        List<User> users = userService.findAll();

        verify(userRepository).findAll();

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("List of users is returned in case when users are exists in storage")
    void list_of_users_is_returned_in_case_when_users_are_exists_in_storage() {
        when(userRepository.findAll()).thenReturn(
            List.of(getUserWithId())
        );

        List<User> users = userService.findAll();

        verify(userRepository).findAll();

        assertThat(users).isEqualTo(
            List.of(getUserWithId())
        );
    }

    @Test
    @DisplayName("User was found by id")
    void user_was_found_by_id() {
        when(userRepository.findById(getUserId())).thenReturn(
            Optional.of(getUserWithId())
        );

        User user = userService.findById(getUserId());

        verify(userRepository).findAll();
        verify(userValidator).validate(getUserId(), getUsers());
        verify(userRepository).findById(getUserId());

        assertThat(user).isEqualTo(getUserWithId());
    }

    @Test
    @DisplayName("User was saved")
    void user_was_saved() {
        when(passwordEncoder.encode(String.valueOf(getPassword())))
            .thenReturn(String.valueOf(getPassword()));

        User user = userService.save(getUserWithId());

        verify(userRepository, atLeast(1)).findAll();
        verify(userValidator).validate(
            getFirstName(),
            getLastName(),
            getEmail(),
            getPhone(),
            getPassword(),
            getUsers()
        );
        verify(passwordEncoder).encode(String.valueOf(getPassword()));
        verify(AdminNumberRepository, atLeast(1)).findAll();
        verify(adminNumberValidator).validateAdminNumberForSignUp(
            getNumber(),
            getAdminNumbers()
        );
        verify(userRepository).save(getUserWithId());

        assertThat(user).isEqualTo(getUserWithId());
    }

    @Test
    @DisplayName("User was updated")
    void user_was_updated() {
        User updatedUser = userService.update(getUserId(), getUpdatedUserWithoutId());

        verify(userRepository).findAll();
        verify(userValidator).validate(getUserId(), getUsers());
        verify(userValidator).validateFullName(getFirstName2(), getLastName2());
        verify(userRepository).update(getUserId(), getUpdatedUserWithId());

        assertThat(updatedUser).isEqualTo(getUpdatedUserWithId());
    }

    @Test
    @DisplayName("User was deleted")
    void user_was_deleted() {
        when(cartRepository.findByUser(getUserWithId()))
            .thenReturn(
                Optional.of(getCartWithId())
            );

        userService.delete(getUserWithId());

        verify(userRepository).findAll();
        verify(userValidator).validate(getUserId(), getUsers());
        verify(cartRepository).delete(getCartWithId());
        verify(userRoleRepository).deleteRoleForUser(getUserId());
        verify(userRepository).delete(getUserWithId());
    }

    @Test
    @DisplayName("User was found by email")
    void user_was_found_by_email() {
        when(userRepository.findByEmail(getEmail())).thenReturn(
            Optional.of(getUserWithId())
        );

        User user = userService.findByEmail(getEmail());

        verify(userValidator).validateEmail(getEmail());
        verify(userRepository).findByEmail(getEmail());

        assertThat(user).isEqualTo(getUserWithId());
    }
}
