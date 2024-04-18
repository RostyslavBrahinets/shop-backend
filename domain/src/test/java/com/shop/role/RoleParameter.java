package com.shop.role;

public class RoleParameter {
    public static Role getRoleWithId() {
        return getRoleWithoutId().withId(getRoleId());
    }

    public static Role getRoleWithId(String name) {
        return getRoleWithoutId(name).withId(getRoleId());
    }

    public static Role getRoleWithoutId() {
        return Role.of(getName());
    }

    public static Role getRoleWithoutId(String name) {
        return Role.of(name);
    }

    public static long getRoleId() {
        return 1L;
    }

    public static long getRoleId2() {
        return 2L;
    }

    public static String getName() {
        return "name";
    }
}
