package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.PersonRoleDao;
import com.shop.repositories.PersonRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        PersonRoleRepository.class,
        PersonRoleRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class PersonRoleRepositoryContextConfigurationTest {
    @Autowired
    private PersonRoleDao personRoleDao;
    @Autowired
    private PersonRoleRepository personRoleRepository;

    @Test
    @DisplayName("Get all products in basket")
    void get_all_products_in_basket() {
        personRoleRepository.findRoleForPerson(1);

        verify(personRoleDao).findRoleForPerson(1);
    }

    @Test
    @DisplayName("Save product to basket")
    void save_product_to_basket() {
        personRoleRepository.saveRoleForPerson(1, 1);

        verify(personRoleDao).saveRoleForPerson(1, 1);
    }

    @Test
    @DisplayName("Delete product from basket")
    void delete_product_from_basket() {
        personRoleRepository.updateRoleForPerson(1, 2);

        verify(personRoleDao).updateRoleForPerson(1, 2);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public PersonRoleDao personRoleDao() {
            return mock(PersonRoleDao.class);
        }
    }
}
