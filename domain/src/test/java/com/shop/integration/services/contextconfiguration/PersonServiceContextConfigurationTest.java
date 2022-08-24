package com.shop.integration.services.contextconfiguration;

import com.shop.models.Person;
import com.shop.models.Product;
import com.shop.repositories.PersonRepository;
import com.shop.services.PersonService;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        PersonService.class,
        PersonServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class PersonServiceContextConfigurationTest {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private ContactValidator contactValidator;
    @Autowired
    private PersonService personService;

    private List<Person> people;

    @BeforeEach
    void setUp() {
        people = List.of();
    }

    @Test
    @DisplayName("Get all people")
    void get_all_people() {
        personService.findAll();

        verify(personRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get person by id")
    void get_person_by_id() {
        long id = 1;

        personService.findById(id);

        verify(personValidator, atLeast(1)).validate(id, people);
        verify(personRepository).findById(id);
    }

    @Test
    @DisplayName("Get person by email")
    void get_person_by_email() {
        String email = "test@email.com";

        personService.findByEmail(email);

        verify(contactValidator, atLeast(1)).validate(email);
        verify(personRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Save person")
    void save_person() {
        String firstName = "John";
        String lastName = "Smith";

        personService.save(firstName, lastName);

        verify(personValidator, atLeast(1)).validate(firstName, lastName);
        verify(personRepository).save(firstName, lastName);
    }

    @Test
    @DisplayName("Update person")
    void update_person() {
        long id = 1;
        String firstName = "John";
        String lastName = "Smith";

        personService.update(id, firstName, lastName);

        verify(personValidator, atLeast(1)).validate(id, people);
        verify(personValidator, atLeast(1)).validate(firstName, lastName);
        verify(personRepository).update(id, firstName, lastName);
    }

    @Test
    @DisplayName("Delete person")
    void delete_person() {
        long id = 1;

        personService.delete(id);

        verify(personValidator, atLeast(1)).validate(id, people);
        verify(personRepository).delete(id);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Person person() {
            return mock(Person.class);
        }

        @Bean
        public PersonRepository personRepository() {
            return mock(PersonRepository.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }

        @Bean
        public ContactValidator contactValidator() {
            return mock(ContactValidator.class);
        }
    }
}
