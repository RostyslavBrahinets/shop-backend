package com.shop.userrole;

import com.shop.role.Role;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(UserRoleController.USER_ROLE_URL)
public class UserRoleController {
    public static final String USER_ROLE_URL = "/api/v1/user-role";
    private final UserRoleService userRoleService;

    public UserRoleController(
        UserRoleService userRoleService
    ) {
        this.userRoleService = userRoleService;
    }

    @GetMapping("/{id}")
    public Role findRoleForUser(
        @PathVariable long id
    ) {
        return userRoleService.findRoleForUser(id);
    }

    @PostMapping("")
    public UserRoleDto saveRoleForUser(
        @RequestBody UserRoleDto userRoleDto
    ) {
        userRoleService.saveRoleForUser(
            userRoleDto.userId(),
            userRoleDto.roleId()
        );
        return userRoleDto;
    }

    @PutMapping("/{id}")
    public String updateRoleForUser(
        @PathVariable long id,
        @RequestBody UserRoleDto userRoleDto
    ) {
        userRoleService.updateRoleForUser(id, userRoleDto.roleId());
        return "Role Successfully Changed";
    }
}
