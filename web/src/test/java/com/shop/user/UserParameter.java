package com.shop.user;

import static com.shop.adminnumber.AdminNumberParameter.getNumber;

public class UserParameter {
    public static User getUserWithId() {
        return User.of(
            "John",
            "Smith",
            getEmail(),
            "+380000000000",
            getPassword(),
            getNumber()
        ).withId(getUserId());
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
