package com.shop.user;

import java.util.List;

import static com.shop.adminnumber.AdminNumberParameter.getNumber;

public class UserParameter {
    public static User getUserWithId() {
        return getUserWithoutId().withId(getUserId());
    }

    public static User getUserWithId(long id) {
        return getUserWithoutId().withId(id);
    }

    public static User getUserWithoutId() {
        return User.of(
            getFirstName(),
            getLastName(),
            getEmail(),
            getPhone(),
            getPassword(),
            getNumber()
        );
    }

    public static User getUserWithoutId(String email, String phone) {
        return User.of(
            getFirstName(),
            getLastName(),
            email,
            phone,
            getPassword(),
            getNumber()
        );
    }

    public static long getUserId() {
        return 1L;
    }

    public static long getUserId2() {
        return 2L;
    }

    static String getFirstName() {
        return "John";
    }

    static String getLastName() {
        return "Smith";
    }

    static String getEmail() {
        return "test@email.com";
    }

    public static String getEmail2() {
        return "test2@email.com";
    }

    static String getPhone() {
        return "+380000000000";
    }

    public static String getPhone2() {
        return "+380000000002";
    }

    static char[] getPassword() {
        return new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    }

    public static List<User> getUsers() {
        return List.of();
    }
}
