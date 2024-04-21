package com.shop.adminnumber;

import java.util.List;

public class AdminNumberParameter {
    public static AdminNumber getAdminNumberWithId() {
        return getAdminNumberWithoutId().withId(getAdminNumberId());
    }

    static AdminNumber getAdminNumberWithId2() {
        return getAdminNumberWithoutId2().withId(getAdminNumberId2());
    }

    static AdminNumber getAdminNumberWithId2(long id) {
        return getAdminNumberWithoutId2().withId(id);
    }

    public static AdminNumber getAdminNumberWithoutId() {
        return AdminNumber.of(getNumber());
    }

    public static AdminNumber getAdminNumberWithoutId2() {
        return AdminNumber.of(getNumber2());
    }

    static long getAdminNumberId() {
        return 1L;
    }

    static long getAdminNumberId2() {
        return 2L;
    }

    public static String getNumber() {
        return "12345678";
    }

    public static String getNumber2() {
        return "87654321";
    }

    public static List<AdminNumber> getAdminNumbers() {
        return List.of();
    }
}
