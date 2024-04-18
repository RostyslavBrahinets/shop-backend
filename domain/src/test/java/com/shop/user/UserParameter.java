package com.shop.user;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.shop.adminnumber.AdminNumberParameter.getNumber;
import static com.shop.adminnumber.AdminNumberParameter.getNumber2;

public class UserParameter {
    public static User getUserWithId() {
        return getUserWithoutId().withId(getUserId());
    }

    public static User getUserWithId(long id) {
        return getUserWithoutId().withId(id);
    }

    public static User getUserWithId2() {
        return getUserWithoutId2().withId(getUserId2());
    }

    public static User getUpdatedUserWithId() {
        return getUpdatedUserWithoutId().withId(getUserId());
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

    public static User getUserWithoutId2() {
        return User.of(
            getFirstName(),
            getLastName(),
            getEmail2(),
            getPhone2(),
            getPassword(),
            getNumber2()
        );
    }

    public static User getUpdatedUserWithoutId() {
        return User.of(
            getFirstName2(),
            getLastName2(),
            getEmail(),
            getPhone(),
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

    public static String getFirstName() {
        return "John";
    }

    public static String getFirstName2() {
        return "Alex";
    }

    public static String getLastName() {
        return "Smith";
    }

    public static String getLastName2() {
        return "Simons";
    }

    public static String getEmail() {
        return "test@email.com";
    }

    public static String getEmail2() {
        return "test2@email.com";
    }

    public static String getPhone() {
        return "+380000000000";
    }

    public static String getPhone2() {
        return "+380000000002";
    }

    public static char[] getPassword() {
        return new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    }

    public static List<User> getUsers() {
        return List.of();
    }

    public static Map<String, Serializable> getMapOfEntries(
        String email,
        String phone,
        String adminNumber
    ) {
        return Map.ofEntries(
            Map.entry("first_name", getFirstName()),
            Map.entry("last_name", getLastName()),
            Map.entry("email", email),
            Map.entry("phone", phone),
            Map.entry("password", String.valueOf(getPassword())),
            Map.entry("admin_number", adminNumber)
        );
    }
}
