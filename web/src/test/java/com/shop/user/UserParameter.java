package com.shop.user;

import static com.shop.adminnumber.AdminNumberParameter.getNumber;

public class UserParameter {
    public static User getUserWithId() {
        return getUserWithoutId().withId(getUserId());
    }

    public static User getUserWithId2() {
        return getUserWithoutId2().withId(getUserId());
    }

    public static User getUserWithoutId() {
        return User.of(
            "John",
            "Smith",
            getEmail(),
            "+380000000000",
            getPassword(),
            getNumber()
        );
    }

    public static User getUserWithoutId2() {
        return User.of(
            "Alex",
            "Simons",
            "test2@email.com",
            "+380000000002",
            getPassword(),
            "87654321"
        );
    }

    public static long getUserId() {
        return 1L;
    }

    public static String getEmail() {
        return "test@email.com";
    }

    public static char[] getPassword() {
        return new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    }
}
