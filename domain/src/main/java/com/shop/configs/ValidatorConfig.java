package com.shop.configs;

import com.shop.repositories.BasketRepository;
import com.shop.repositories.ContactRepository;
import com.shop.repositories.PersonRepository;
import com.shop.repositories.ProductRepository;
import com.shop.validators.BasketValidator;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import com.shop.validators.ProductValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryConfig.class)
public class ValidatorConfig {
    @Bean
    public BasketValidator contactValidator(BasketRepository basketRepository) {
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
    public ProductValidator personValidator(ProductRepository productRepository) {
        return new ProductValidator(productRepository);
    }
}
