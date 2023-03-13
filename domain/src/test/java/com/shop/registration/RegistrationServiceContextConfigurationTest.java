package com.shop.registration;

import com.shop.admin_number.AdminNumber;
import com.shop.admin_number.AdminNumberService;
import com.shop.cart.CartService;
import com.shop.user.User;
import com.shop.stripe.StripePayment;
import com.shop.user.UserService;
import com.shop.user_role.UserRoleService;
import com.shop.wallet.WalletService;
import com.stripe.exception.StripeException;
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
        RegistrationService.class,
        RegistrationServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class RegistrationServiceContextConfigurationTest {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RegistrationValidator registrationValidator;

    @Test
    @DisplayName("Registration")
    void registration() throws StripeException {
        String firstName = "John";
        String lastName = "Smith";
        String email = "test@email.com";
        String phone = "+380000000000";
        String password = "password";
        String adminNumber = "12345678";
        List<User> users = List.of();
        List<AdminNumber> adminNumbers = List.of();

        registrationService.registration(
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumber
        );

        verify(registrationValidator).isValidData(
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
        public RegistrationValidator registrationValidator() {
            return mock(RegistrationValidator.class);
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

        @Bean
        public WalletService walletService() {
            return mock(WalletService.class);
        }

        @Bean
        public StripePayment stripePayment() {
            return mock(StripePayment.class);
        }
    }
}
