package com.shop.services;

import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    private Contact contact;
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

    @Test
    @DisplayName("Get all contacts")
    void get_all_contacts() {
        contactService.findAll();

        verify(contactRepository).findAll();
    }

    @Test
    @DisplayName("Get contact by id")
    void get_contact_by_id() {
        long id = 1;

        contactService.findById(id);

        verify(contactValidator, atLeast(1)).validate(id);
        verify(contactRepository).findById(id);
    }

    @Test
    @DisplayName("Get contact by person")
    void get_contact_by_person() {
        long personId = 1;

        contactService.findByPerson(personId);

        verify(personValidator, atLeast(1)).validate(personId);
        verify(contactRepository).findByPerson(personId);
    }

    @Test
    @DisplayName("Save contact")
    void save_contact() {
        long personId = 1;

        contactService.save(contact, personId);

        verify(contactValidator, atLeast(1)).validate(contact);
        verify(personValidator, atLeast(1)).validate(personId);
        verify(passwordEncoder, atLeast(1)).encode(contact.getPassword());
        verify(contactRepository).save(contact, personId);
    }

    @Test
    @DisplayName("Update contact")
    void update_contact() {
        long id = 1;

        contactService.update(id, contact);

        verify(contactValidator, atLeast(1)).validate(id);
        verify(contactValidator, atLeast(1)).validate(contact);
        verify(contactRepository).update(id, contact);
    }

    @Test
    @DisplayName("Delete contact")
    void delete_contact() {
        long id = 1;

        contactService.delete(id);

        verify(contactValidator, atLeast(1)).validate(id);
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
        public PersonValidator personValidator() {
            return mock(PersonValidator.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return mock(PasswordEncoder.class);
        }
    }
}
