package com.shop.repositories.contextconfiguration;

import com.shop.dao.PersonDao;
import com.shop.models.Person;
import com.shop.repositories.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        PersonRepository.class,
        PersonRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class PersonRepositoryContextConfigurationTest {
    @Autowired
    private Person person;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("Get all people")
    void get_all_people() {
        personRepository.findAll();

        verify(personDao, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get person by id")
    void get_basket_by_id() {
        long id = 1;

        personRepository.findById(id);

        verify(personDao).findById(id);
    }

    @Test
    @DisplayName("Get person by email")
    void get_person_by_email() {
        String email = "test@email.com";

        personRepository.findByEmail(email);

        verify(personDao).findByEmail(email);
    }

    @Test
    @DisplayName("Save person")
    void save_person() {
        personRepository.save(person);

        verify(personDao).save(
            person.getFirstName(),
            person.getLastName()
        );
    }

    @Test
    @DisplayName("Update person")
    void update_person() {
        long id = 1;

        personRepository.update(id, person);

        verify(personDao).update(
            id,
            person.getFirstName(),
            person.getLastName()
        );
    }

    @Test
    @DisplayName("Delete person")
    void delete_person() {
        long id = 1;

        personRepository.delete(id);

        verify(personDao).delete(id);
    }

    @Test
    @DisplayName("Count people")
    void count_people() {
        personRepository.count();

        verify(personDao, atLeast(1)).findAll();
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Person person() {
            return mock(Person.class);
        }

        @Bean
        public PersonDao personDao() {
            return mock(PersonDao.class);
        }
    }
}
