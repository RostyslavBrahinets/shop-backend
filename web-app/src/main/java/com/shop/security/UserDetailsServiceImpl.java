package com.shop.security;

import com.shop.models.Role;
import com.shop.services.UserRoleService;
import com.shop.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final UserRoleService userRoleService;

    public UserDetailsServiceImpl(
        UserService userService,
        UserRoleService userRoleService
    ) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Role role = userRoleService.findRoleForUser(userService.findByEmail(email).getId());
        List<GrantedAuthority> grantedAuthorities = List.of(
            new SimpleGrantedAuthority(role.getName())
        );

        return new User(
            Objects.requireNonNull(userService.findByEmail(email)).getEmail(),
            userService.findByEmail(email).getPassword(),
            grantedAuthorities
        );
    }
}
