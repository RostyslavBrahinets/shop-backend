package com.shop.user;

import com.shop.role.Role;

public class RoleParameter {
    public static Role getRoleWithId() {
        return Role.of("name").withId(1L);
    }
}
