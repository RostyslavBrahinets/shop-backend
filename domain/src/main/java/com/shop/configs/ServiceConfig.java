package com.shop.configs;

import com.shop.repositories.BasketRepository;
import com.shop.repositories.ContactRepository;
import com.shop.repositories.PersonRepository;
import com.shop.repositories.ProductRepository;
import com.shop.services.BasketService;
import com.shop.services.ContactService;
import com.shop.services.PersonService;
import com.shop.services.ProductService;
import com.shop.validators.BasketValidator;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.ProductValidator;
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
}
