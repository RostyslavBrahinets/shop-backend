package com.shop.configs;

import com.shop.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {
    @Bean
    public BasketDao basketDao() {
        return new BasketDao();
    }

    @Bean
    public ContactDao contactDao() {
        return new ContactDao();
    }

    @Bean
    public PersonDao personDao() {
        return new PersonDao();
    }

    @Bean
    public ProductDao productDao() {
        return new ProductDao();
    }

    @Bean
    public WalletDao walletDao() {
        return new WalletDao();
    }
}
