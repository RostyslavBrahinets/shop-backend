package com.shop.security;

import com.shop.role.Role;
import com.shop.user.UserService;
import com.shop.userrole.UserRoleService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
            Arrays.toString(userService.findByEmail(email).getPassword()),
            grantedAuthorities
        );
    }
}
