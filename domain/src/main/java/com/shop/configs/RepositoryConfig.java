package com.shop.configs;

import com.shop.dao.BasketDao;
import com.shop.dao.ContactDao;
import com.shop.repositories.BasketRepository;
import com.shop.repositories.ContactRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DaoConfig.class)
public class RepositoryConfig {
    @Bean
    public ContactRepository contactRepository(ContactDao contactDao) {
        return new ContactRepository(contactDao);
    }

    @Bean
    public BasketRepository basketRepository(BasketDao basketDao) {
        return new BasketRepository(basketDao);
    }
}
