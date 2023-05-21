package com.shop.configs;

import com.shop.security.SignInPasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    void configureGlobal(
        AuthenticationManagerBuilder auth,
        SignInPasswordAuthenticationProvider signInPasswordAuthenticationProvider
    ) throws Exception {
        auth
            .authenticationProvider(signInPasswordAuthenticationProvider)
            .jdbcAuthentication()
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors().and()
            .csrf()
            .csrfTokenRepository(
                CookieCsrfTokenRepository.withHttpOnlyFalse()
            )
            .and()
            .authorizeHttpRequests(
                authz -> authz
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers(
                        "/sign-up", "/error", "/", "/categories/**", "/products/**",
                        "/api**", "/api/sign-up",
                        "/api/users/**", "/api/user-role/**", "/api/admins-numbers/**",
                        "/api/carts/**", "/api/categories/**",
                        "/api/products/**", "/api/product-category/**",
                        "/api/payment", "/api/report/**",
                        "/js/**", "/images/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .build();
    }
}
