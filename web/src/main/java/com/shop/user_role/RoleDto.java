package com.shop.user_role;

import org.springframework.stereotype.Component;

@Component
public class RoleDto {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
