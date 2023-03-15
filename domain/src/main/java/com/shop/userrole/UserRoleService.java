package com.shop.userrole;

import com.shop.role.Role;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import com.shop.role.RoleValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleValidator roleValidator;

    public UserRoleService(
        UserRoleRepository userRoleRepository,
        UserService userService,
        UserValidator userValidator,
        RoleValidator roleValidator
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleValidator = roleValidator;
    }

    public Role findRoleForUser(long userId) {
        userValidator.validate(userId, userService.findAll());
        Optional<Role> role = userRoleRepository.findRoleForUser(userId);
        return role.orElseGet(Role::new);
    }

    public void saveRoleForUser(long userId, long roleId) {
        userValidator.validate(userId, userService.findAll());
        roleValidator.validate(roleId);
        userRoleRepository.saveRoleForUser(userId, roleId);
    }

    public void updateRoleForUser(long userId, long roleId) {
        userValidator.validate(userId, userService.findAll());
        roleValidator.validate(roleId);
        userRoleRepository.updateRoleForUser(userId, roleId);
    }
}
