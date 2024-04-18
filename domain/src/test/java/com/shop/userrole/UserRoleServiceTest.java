package com.shop.userrole;

import com.shop.role.RoleValidator;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.shop.role.RoleParameter.getRoleId;
import static com.shop.user.UserParameter.getUserId;
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
        userRoleService.saveRoleForUser(getUserId(), getRoleId());
        verify(userRoleRepository).saveRoleForUser(getUserId(), getRoleId());
    }
}
