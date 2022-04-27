package com.shop.configs;

import com.shop.repositories.*;
import com.shop.services.*;
import com.shop.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RepositoryConfig.class, ValidatorConfig.class})
public class ServiceConfig {
    @Bean
    public BasketService basketService(
        BasketRepository basketRepository,
        BasketValidator basketValidator
    ) {
        return new BasketService(basketRepository, basketValidator);
    }

    @Bean
    public ContactService contactService(
        ContactRepository contactRepository,
        ContactValidator contactValidator
    ) {
        return new ContactService(contactRepository, contactValidator);
    }

    @Bean
    public PersonService personService(
        PersonRepository personRepository,
        PersonValidator personValidator
    ) {
        return new PersonService(personRepository, personValidator);
    }

    @Bean
    public ProductService personService(
        ProductRepository productRepository,
        ProductValidator productValidator
    ) {
        return new ProductService(productRepository, productValidator);
    }

    @Bean
    public WalletService personService(
        WalletRepository walletRepository,
        WalletValidator walletValidator
    ) {
        return new WalletService(walletRepository, walletValidator);
    }
}
