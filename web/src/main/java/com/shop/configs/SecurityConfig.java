package com.shop.configs;

import com.shop.security.SignInPasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http
            .authorizeRequests()
            .mvcMatchers(
                "/sign-up", "/error", "/", "/categories/**", "/products/**",
                "/api/sign-up", "/api**", "/api/categories/**",
                "/api/products/**", "/api/product-category/**", "/js/**", "/images/**"
            ).permitAll()
            .mvcMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/sign-in")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .permitAll()
            .and()
            .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .logoutUrl("/sign-out")
            .logoutSuccessUrl("/sign-in?sign-out")
            .permitAll();
    }
}
