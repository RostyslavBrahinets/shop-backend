package com.shop.user;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserValidatorTest {
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userValidator = new UserValidator();
    }

    @Test
    @DisplayName("User validated without exceptions")
    void user_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because first name of user is null")
    void throw_validation_exception_because_first_name_of_user_is_null() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                null,
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because first name of user is empty")
    void throw_validation_exception_because_first_name_of_user_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because last name of user is null")
    void throw_validation_exception_because_last_name_of_user_is_null() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                null,
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because last name of user is empty")
    void throw_validation_exception_because_last_name_of_user_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }


    @Test
    @DisplayName("Throw ValidationException because email of user is null")
    void throw_validation_exception_because_email_of_user_is_null() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                null,
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of user is empty")
    void throw_validation_exception_because_email_of_user_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of user starts with char '@'")
    void throw_validation_exception_because_email_of_user_starts_with_char_at() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of user not contains char '@'")
    void throw_validation_exception_because_email_of_user_not_contains_char_at() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test.email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of user not ends '.com'")
    void throw_validation_exception_because_email_of_user_not_ends_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of user ends '@.com'")
    void throw_validation_exception_because_email_of_user_ends_char_at_and_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email of user already in use")
    void throw_validation_exception_because_email_of_user_already_in_use() {
        List<User> users = List.of(
            new User(
                1,
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                1
            )
        );

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                users
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of user is null")
    void throw_validation_exception_because_phone_of_user_is_null() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                null,
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of user is empty")
    void throw_validation_exception_because_phone_of_user_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone of user not starts with '+'")
    void throw_validation_exception_because_phone_of_user_not_starts_with_plus() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because length of phone of user less then expected")
    void throw_validation_exception_because_phone_of_user_less_then_expected() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because password of user is null")
    void throw_validation_exception_because_password_of_user_is_null() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                null,
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Throw ValidationException because password of user is empty")
    void throw_validation_exception_because_password_of_user_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{},
                List.of()
            )
        );
    }

    @Test
    @DisplayName("Phone validated without exceptions")
    void phone_validated_without_exceptions() {
        User user = User.of(
            "John",
            "Smith",
            "test@email.com",
            "+380000000000",
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            1
        ).withId(1);

        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertDoesNotThrow(
            () -> userValidator.validatePhone("+380000000000", user, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone is null")
    void throw_validation_exception_because_phone_is_null() {
        User user = User.of(
            "John",
            "Smith",
            "test@email.com",
            null,
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            1
        ).withId(1);

        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertThrows(
            ValidationException.class,
            () -> userValidator.validatePhone(null, user, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone is empty")
    void throw_validation_exception_because_phone_is_empty() {
        User user = User.of(
            "John",
            "Smith",
            "test@email.com",
            "",
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            1
        ).withId(1);

        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertThrows(
            ValidationException.class,
            () -> userValidator.validatePhone("", user, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because phone not starts with '+'")
    void throw_validation_exception_because_phone_not_starts_with_plus() {
        User user = User.of(
            "John",
            "Smith",
            "test@email.com",
            "380000000000",
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            1
        ).withId(1);

        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertThrows(
            ValidationException.class,
            () -> userValidator.validatePhone("380000000000", user, List.of())
        );
    }

    @Test
    @DisplayName("Throw ValidationException because length of phone less then expected")
    void throw_validation_exception_because_length_of_phone_less_then_expected() {
        User user = User.of(
            "John",
            "Smith",
            "test@email.com",
            "+380",
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            1
        ).withId(1);

        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertThrows(
            ValidationException.class,
            () -> userValidator.validatePhone("+380", user, List.of())
        );
    }

    @Test
    @DisplayName("Id of user validated without exceptions")
    void id_of_user_validated_without_exceptions() {
        List<User> users = List.of(new User(
            1,
            "John",
            "Smith",
            "test@email.com",
            "+380000000000",
            new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            1
        ));

        when(userRepository.findAll()).thenReturn(users);

        assertDoesNotThrow(() -> userValidator.validate(1, users));
    }

    @Test
    @DisplayName("Throw NotFoundException because id of user not found")
    void throw_not_found_exception_because_id_of_user_not_found() {
        assertThrows(
            NotFoundException.class,
            () -> userValidator.validate(1, List.of())
        );
    }

    @Test
    @DisplayName("Email of user validated without exceptions")
    void email_of_user_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> userValidator.validateEmail("test@email.com")
        );
    }

    @Test
    @DisplayName("Email of admin validated without exceptions")
    void email_of_admin_validated_without_exceptions() {
        assertDoesNotThrow(
            () -> userValidator.validateEmail("admin")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email is null")
    void throw_validation_exception_because_email_is_null() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail(null)
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email is empty")
    void throw_validation_exception_because_email_is_empty() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail("")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email starts with char '@'")
    void throw_validation_exception_because_email_starts_with_char_at() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail("@email.com")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email not contains char '@'")
    void throw_validation_exception_because_email_not_contains_char_at() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail("test.email.com")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email not ends '.com'")
    void throw_validation_exception_because_email_not_ends_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail("test@email")
        );
    }

    @Test
    @DisplayName("Throw ValidationException because email ends '@.com'")
    void throw_validation_exception_because_email_ends_char_at_and_dot_com() {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail("test@.com")
        );
    }
}
