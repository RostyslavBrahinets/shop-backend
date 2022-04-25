package com.shop.configs;

import com.shop.dao.ContactDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {
    @Bean
    public ContactDao contactDao() {
        return new ContactDao();
    }
}
