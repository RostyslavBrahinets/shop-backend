package com.shop.unit.services;

import com.shop.models.Role;
import com.shop.repositories.RoleRepository;
import com.shop.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(roleRepository.findByName("name")).thenReturn(
            Optional.of(Role.of("name").withId(1))
        );

        Role role = roleService.findByName("name");

        assertThat(role).isEqualTo(new Role(1, "name"));
    }
}
