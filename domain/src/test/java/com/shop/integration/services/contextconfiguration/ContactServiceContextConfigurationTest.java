package com.shop.integration.services.contextconfiguration;

import com.shop.models.Contact;
import com.shop.models.Person;
import com.shop.repositories.ContactRepository;
import com.shop.services.ContactService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ContactService.class,
        ContactServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class ContactServiceContextConfigurationTest {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactValidator contactValidator;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ContactService contactService;
    @Autowired
    private PersonService personService;

    private List<Contact> contacts;
    private List<Person> people;

    @BeforeEach
    void setUp() {
        contacts = List.of();
        people = List.of();
    }

    @Test
    @DisplayName("Get all contacts")
    void get_all_contacts() {
        contactService.findAll();

        verify(contactRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get contact by id")
    void get_contact_by_id() {
        long id = 1;

        contactService.findById(id);

        verify(contactValidator, atLeast(1)).validate(id, contacts);
        verify(contactRepository, atLeast(1)).findById(id);
    }

    @Test
    @DisplayName("Get contact by person")
    void get_contact_by_person() {
        long personId = 1;

        contactService.findByPerson(personId);

        verify(personValidator, atLeast(1)).validate(personId, people);
        verify(contactRepository).findByPerson(personId);
    }

    @Test
    @DisplayName("Save contact")
    void save_contact() {
        long personId = 1;
        String email = "test@email.com";
        String phone = "+380000000000";
        String password = "password";

        contactService.save(email, phone, password, personId);

        verify(contactRepository, atLeast(1)).findAll();
        verify(contactValidator, atLeast(1)).validate(email, phone, password, contacts);
        verify(personService, atLeast(1)).findAll();
        verify(personValidator, atLeast(1)).validate(personId, people);
        verify(passwordEncoder, atLeast(1)).encode(password);
        verify(contactRepository).save(email, phone, null, personId);
    }

    @Test
    @DisplayName("Update contact")
    void update_contact() {
        long id = 1;
        String phone = "+380000000000";

        contactService.update(id, phone);

        verify(contactRepository, atLeast(1)).findAll();
        verify(contactRepository).findById(id);
        verify(contactValidator, atLeast(1)).validate(id, contacts);
        verify(contactRepository).update(id, phone);
    }

    @Test
    @DisplayName("Delete contact")
    void delete_contact() {
        long id = 1;

        contactService.delete(id);

        verify(contactValidator, atLeast(1)).validate(id, contacts);
        verify(contactRepository).delete(id);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Contact contact() {
            return mock(Contact.class);
        }

        @Bean
        public ContactRepository contactRepository() {
            return mock(ContactRepository.class);
        }

        @Bean
        public ContactValidator contactValidator() {
            return mock(ContactValidator.class);
        }

        @Bean
        public PersonService personService() {
            return mock(PersonService.class);
        }

        @Bean
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return mock(PasswordEncoder.class);
        }
    }
}
