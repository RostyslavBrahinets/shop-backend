package com.shop.configs;

import com.shop.repositories.*;
import com.shop.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryConfig.class)
public class ValidatorConfig {
    @Bean
    public BasketValidator basketValidator(BasketRepository basketRepository) {
        return new BasketValidator(basketRepository);
    }

    @Bean
    public ContactValidator contactValidator(ContactRepository contactRepository) {
        return new ContactValidator(contactRepository);
    }

    @Bean
    public PersonValidator personValidator(PersonRepository personRepository) {
        return new PersonValidator(personRepository);
    }

    @Bean
    public ProductValidator productValidator(ProductRepository productRepository) {
        return new ProductValidator(productRepository);
    }

    @Bean
    public WalletValidator walletValidator(WalletRepository walletRepository) {
        return new WalletValidator(walletRepository);
    }
}
