package com.shop.integration.services.contextconfiguration;

import com.shop.models.Contact;
import com.shop.services.*;
import com.shop.stripe.StripePayment;
import com.shop.validators.RegistrationValidator;
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
        List<Contact> contacts = List.of();

        registrationService.registration(
            firstName,
            lastName,
            email,
            phone,
            password
        );

        verify(registrationValidator).isValidData(
            firstName,
            lastName,
            email,
            phone,
            password,
            contacts
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public RegistrationValidator registrationValidator() {
            return mock(RegistrationValidator.class);
        }

        @Bean
        public PersonService personService() {
            return mock(PersonService.class);
        }

        @Bean
        public ContactService contactService() {
            return mock(ContactService.class);
        }

        @Bean
        public PersonRoleService personRoleService() {
            return mock(PersonRoleService.class);
        }

        @Bean
        public BasketService basketService() {
            return mock(BasketService.class);
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
