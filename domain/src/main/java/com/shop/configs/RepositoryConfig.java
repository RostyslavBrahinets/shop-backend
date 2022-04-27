package com.shop.configs;

import com.shop.dao.*;
import com.shop.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DaoConfig.class)
public class RepositoryConfig {
    @Bean
    public BasketRepository basketRepository(BasketDao basketDao) {
        return new BasketRepository(basketDao);
    }

    @Bean
    public ContactRepository contactRepository(ContactDao contactDao) {
        return new ContactRepository(contactDao);
    }

    @Bean
    public PersonRepository personRepository(PersonDao personDao) {
        return new PersonRepository(personDao);
    }

    @Bean
    public ProductRepository productRepository(ProductDao productDao) {
        return new ProductRepository(productDao);
    }

    @Bean
    public WalletRepository walletRepository(WalletDao walletDao) {
        return new WalletRepository(walletDao);
    }

    @Bean
    public ProductsBasketsRepository productsBasketsRepository(
        ProductsBasketsDao productsBasketsDao
    ) {
        return new ProductsBasketsRepository(productsBasketsDao);
    }
}
