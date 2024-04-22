package com.shop.userrole;

import static com.shop.user.UserParameter.getUserId;

public class UserRoleParameter {
    public static UserRoleDto getUserRole() {
        return new UserRoleDto(getUserId(), 1L);
    }
}
