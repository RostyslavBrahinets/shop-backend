package com.shop.controllers;

import com.shop.dto.RoleDto;
import com.shop.models.Role;
import com.shop.services.UserRoleService;
import com.shop.services.RoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserRoleController.USER_ROLE_URL)
public class UserRoleController {
    public static final String USER_ROLE_URL = "/web-api/user-role";
    private final UserRoleService userRoleService;
    private final RoleService roleService;

    public UserRoleController(
        UserRoleService userRoleService,
        RoleService roleService
    ) {
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public Role findRoleForUser(
        @PathVariable long id
    ) {
        return userRoleService.findRoleForUser(id);
    }

    @PostMapping("/{id}")
    public String updateRoleForUser(
        @PathVariable long id,
        @RequestBody RoleDto roleDto
    ) {
        Role role = roleService.findByName(roleDto.getRole());
        userRoleService.updateRoleForUser(id, role.getId());
        return "Role Successfully Changed";
    }
}
