package com.shop.user;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserValidatorTest {
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

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid first name of user")
    void throw_validation_exception_for_invalid_first_name_of_user(String firstName, List<User> users) {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                firstName,
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                users
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid last name of user")
    void throw_validation_exception_for_invalid_last_name_of_user(String lastName, List<User> users) {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                lastName,
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                users
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationEmailOfUserTestCases")
    @DisplayName("Throw ValidationException for invalid email of user")
    void throw_validation_exception_for_invalid_email_of_user(String email, List<User> users) {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                email,
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                users
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationPhoneOfUserTestCases")
    @DisplayName("Throw ValidationException for invalid phone of user")
    void throw_validation_exception_for_invalid_phone_of_user(String phone, List<User> users) {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                phone,
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                users
            )
        );
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("Throw ValidationException for invalid password of user")
    void throw_validation_exception_for_invalid_password_of_user(String password, List<User> users) {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validate(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                password == null ? null : password.toCharArray(),
                users
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
            "12345678"
        ).withId(1);

        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertDoesNotThrow(
            () -> userValidator.validatePhone("+380000000000", user, List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("validationPhoneTestCases")
    @DisplayName("Throw ValidationException for invalid phone")
    void throw_validation_exception_for_invalid_phone(String phone, User user, List<User> users) {
        when(userRepository.findById(1))
            .thenReturn(Optional.of(
                user
            ));

        assertThrows(
            ValidationException.class,
            () -> userValidator.validatePhone(phone, user, users)
        );
    }

    @Test
    @DisplayName("Id of user validated without exceptions")
    void id_of_user_validated_without_exceptions() {
        List<User> users = List.of(
            User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                )
                .withId(1)
        );

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

    @ParameterizedTest
    @MethodSource("validationEmailTestCases")
    @DisplayName("Throw ValidationException for invalid email")
    void throw_validation_exception_for_invalid_email(String email) {
        assertThrows(
            ValidationException.class,
            () -> userValidator.validateEmail(email)
        );
    }

    private static Stream<Arguments> validationTestCases() {
        List<User> users = List.of(
            User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                )
                .withId(1)
        );

        return Stream.of(
            Arguments.of(null, users),
            Arguments.of("", users)
        );
    }

    private static Stream<Arguments> validationEmailOfUserTestCases() {
        List<User> users = List.of(
            User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                )
                .withId(1)
        );

        return Stream.of(
            Arguments.of(null, users),
            Arguments.of("", users),
            Arguments.of("@email.com", users),
            Arguments.of("test.email.com", users),
            Arguments.of("test@email", users),
            Arguments.of("test@.com", users),
            Arguments.of("test@email.com", users)
        );
    }

    private static Stream<Arguments> validationPhoneOfUserTestCases() {
        List<User> users = List.of(
            User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                )
                .withId(1)
        );

        return Stream.of(
            Arguments.of(null, users),
            Arguments.of("", users),
            Arguments.of("380000000000", users),
            Arguments.of("+380", users)
        );
    }

    private static Stream<Arguments> validationPhoneTestCases() {
        List<User> users = List.of(
            User.of(
                    "John",
                    "Smith",
                    "test@email.com",
                    "+380000000000",
                    new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                    "12345678"
                )
                .withId(1)
        );

        User user = User.of(
                "John",
                "Smith",
                "test@email.com",
                "+380000000000",
                new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
                "12345678"
            )
            .withId(1);

        return Stream.of(
            Arguments.of(null, user, users),
            Arguments.of("", user, users),
            Arguments.of("380000000000", user, users),
            Arguments.of("+380", user, users)
        );
    }

    private static Stream<Arguments> validationEmailTestCases() {
        return Stream.of(
            Arguments.of((Object) null),
            Arguments.of(""),
            Arguments.of("@email.com"),
            Arguments.of("test.email.com"),
            Arguments.of("test@email"),
            Arguments.of("test@.com")
        );
    }
}
