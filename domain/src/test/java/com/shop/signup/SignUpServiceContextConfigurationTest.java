package com.shop.signup;

import com.shop.adminnumber.AdminNumberService;
import com.shop.cart.CartService;
import com.shop.user.UserService;
import com.shop.userrole.UserRoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.shop.adminnumber.AdminNumberParameter.getAdminNumbers;
import static com.shop.adminnumber.AdminNumberParameter.getNumber;
import static com.shop.user.UserParameter.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        SignUpService.class,
        SignUpServiceContextConfigurationTest.TestContextConfig.class
    }
)
class SignUpServiceContextConfigurationTest {
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private SignUpValidator signUpValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminNumberService adminNumberService;

    @Test
    @DisplayName("Sign Up")
    void signUp() {
        when(userService.findAll())
            .thenReturn(List.of(getUserWithId()));

        signUpService.signUp(
            getFirstName(),
            getLastName(),
            getEmail(),
            getPhone(),
            getPassword(),
            getNumber()
        );

        verify(userService, atLeast(1)).findAll();
        verify(adminNumberService).findAll();
        verify(signUpValidator).isValidData(
            getFirstName(),
            getLastName(),
            getEmail(),
            getPhone(),
            getPassword(),
            getNumber(),
            List.of(getUserWithId()),
            getAdminNumbers()
        );
        verify(userService, atLeast(1)).findAll();
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public SignUpValidator signUpValidator() {
            return mock(SignUpValidator.class);
        }

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public AdminNumberService adminNumberService() {
            return mock(AdminNumberService.class);
        }

        @Bean
        public UserRoleService userRoleService() {
            return mock(UserRoleService.class);
        }

        @Bean
        public CartService cartService() {
            return mock(CartService.class);
        }
    }
}
