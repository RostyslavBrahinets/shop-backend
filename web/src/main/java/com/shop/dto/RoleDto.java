package com.shop.dto;

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
