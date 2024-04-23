package com.shop.adminnumber;

public class AdminNumberParameter {
    static AdminNumber getAdminNumberWithId() {
        return getAdminNumberWithoutId().withId(getAdminNumberId());
    }

    static AdminNumber getAdminNumberWithId2() {
        return getAdminNumberWithoutId2().withId(getAdminNumberId());
    }

    static AdminNumber getAdminNumberWithoutId() {
        return AdminNumber.of(getNumber());
    }

    static AdminNumber getAdminNumberWithoutId2() {
        return AdminNumber.of("87654321");
    }

    static long getAdminNumberId() {
        return 1L;
    }

    public static String getNumber() {
        return "12345678";
    }
}
