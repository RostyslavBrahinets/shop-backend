package com.shop.configs;

import com.shop.dao.BasketDao;
import com.shop.dao.ContactDao;
import com.shop.dao.PersonDao;
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
}
