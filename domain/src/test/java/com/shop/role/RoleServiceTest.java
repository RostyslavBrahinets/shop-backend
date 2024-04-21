package com.shop.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.shop.role.RoleParameter.getName;
import static com.shop.role.RoleParameter.getRoleWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roleService = new RoleService(
            roleRepository
        );
    }

    @Test
    @DisplayName("Role was found by name")
    void role_was_found_by_name() {
        when(roleRepository.findByName("name"))
            .thenReturn(
                Optional.of(getRoleWithId())
            );

        Role role = roleService.findByName(getName());

        verify(roleRepository).findByName(getName());

        assertThat(role).isEqualTo(getRoleWithId());
    }
}
