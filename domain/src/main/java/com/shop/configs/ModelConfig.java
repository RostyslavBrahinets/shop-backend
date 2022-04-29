package com.shop.configs;

import com.shop.models.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    @Bean
    public Basket basket() {
        return new Basket();
    }

    @Bean
    public Contact contact() {
        return new Contact();
    }

    @Bean
    public Person person() {
        return new Person();
    }

    @Bean
    public Product product() {
        return new Product();
    }

    @Bean
    public Wallet wallet() {
        return new Wallet();
    }
}
