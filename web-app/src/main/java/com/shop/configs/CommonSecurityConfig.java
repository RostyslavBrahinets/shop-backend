package com.shop.configs;

import com.shop.security.UserDetailsServiceImpl;
import com.shop.services.ContactService;
import com.shop.services.PersonService;
import com.shop.services.RoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CommonSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(
        PersonService personService,
        ContactService contactService,
        RoleService roleService
    ) {
        return new UserDetailsServiceImpl(personService, contactService, roleService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
