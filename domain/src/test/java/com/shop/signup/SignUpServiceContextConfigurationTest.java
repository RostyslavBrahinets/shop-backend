package com.shop.signup;

import com.shop.adminnumber.AdminNumber;
import com.shop.adminnumber.AdminNumberService;
import com.shop.cart.CartService;
import com.shop.user.User;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        SignUpService.class,
        SignUpServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class SignUpServiceContextConfigurationTest {
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private SignUpValidator signUpValidator;

    @Test
    @DisplayName("Sign Up")
    void signUp() {
        String firstName = "John";
        String lastName = "Smith";
        String email = "test@email.com";
        String phone = "+380000000000";
        char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        String adminNumber = "12345678";
        List<User> users = List.of();
        List<AdminNumber> adminNumbers = List.of();

        signUpService.signUp(
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumber
        );

        verify(signUpValidator).isValidData(
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumber,
            users,
            adminNumbers
        );
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
