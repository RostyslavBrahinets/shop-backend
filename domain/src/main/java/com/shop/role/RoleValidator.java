package com.shop.role;

import com.shop.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {
    public void validate(long id) {
        if (id < 1 || id > 2) {
            throw new NotFoundException("Role not found");
        }
    }
}