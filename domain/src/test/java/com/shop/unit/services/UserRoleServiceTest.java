package com.shop.unit.services;

import com.shop.repositories.UserRoleRepository;
import com.shop.services.UserRoleService;
import com.shop.services.UserService;
import com.shop.validators.UserValidator;
import com.shop.validators.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class UserRoleServiceTest {
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private RoleValidator roleValidator;

    private UserRoleService userRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userRoleService = new UserRoleService(
            userRoleRepository,
            userService,
            userValidator,
            roleValidator
        );
    }

    @Test
    @DisplayName("Role for user was saved")
    void role_for_user_was_saved() {
        userRoleService.saveRoleForUser(1, 1);
        verify(userRoleRepository).saveRoleForUser(1, 1);
    }
}
