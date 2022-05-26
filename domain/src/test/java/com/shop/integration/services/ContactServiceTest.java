package com.shop.integration.services;

import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import com.shop.services.ContactService;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContactServiceTest {
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ContactValidator contactValidator;
    @Mock
    private PersonValidator personValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        contactService = new ContactService(
            contactRepository,
            contactValidator,
            personValidator,
            passwordEncoder
        );
    }

    @Test
    @DisplayName("Contact was saved for with correct input")
    void contact_was_saved_with_correct_input() {
        when(contactRepository.save(Contact.of(
            "test@email.com",
            "+380000000000",
            "password"
        ).withId(1), 1))
            .thenReturn(Contact.of(
                "test@email.com",
                "+380000000000",
                "password"
            ).withId(1));

        Contact savedBasket = contactService.save(Contact.of(
            "test@email.com",
            "+380000000000",
            "password"
        ).withId(1), 1);

        assertThat(savedBasket).isEqualTo(new Contact(
            1,
            "test@email.com",
            "+380000000000",
            passwordEncoder.encode("password")
        ));
    }

    @Test
    @DisplayName("Empty list of contacts is returned in case when no contacts in storage")
    void empty_list_of_baskets_is_returned_in_case_when_no_baskets_in_storage() {
        when(contactRepository.findAll()).thenReturn(emptyList());

        List<Contact> contacts = contactService.findAll();

        assertThat(contacts).isEmpty();
    }

    @Test
    @DisplayName("List of contacts is returned in case when contacts are exists in storage")
    void list_of_contacts_is_returned_in_case_when_contacts_are_exists_in_storage() {
        when(contactRepository.findAll()).thenReturn(
            List.of(
                Contact.of(
                        "test@email.com",
                        "+380000000000",
                        "password"
                    )
                    .withId(1)
            )
        );

        List<Contact> contacts = contactService.findAll();

        assertThat(contacts).isEqualTo(List.of(new Contact(
            1,
            "test@email.com",
            "+380000000000",
            "password"
        )));
    }

    @Test
    @DisplayName("Basket was found by id")
    void basket_was_found_by_id() {
        when(contactRepository.findById(1)).thenReturn(
            Optional.of(
                Contact.of(
                        "test@email.com",
                        "+380000000000",
                        "password"
                    )
                    .withId(1)
            )
        );

        Contact contact = contactService.findById(1);

        assertThat(contact).isEqualTo(new Contact(
            1,
            "test@email.com",
            "+380000000000",
            "password"
        ));
    }

    @Test
    @DisplayName("Contact was found by person")
    void basket_was_found_by_person() {
        when(contactRepository.findByPerson(1)).thenReturn(
            Optional.of(
                Contact.of(
                        "test@email.com",
                        "+380000000000",
                        "password"
                    )
                    .withId(1)
            )
        );

        Contact contact = contactService.findByPerson(1);

        assertThat(contact).isEqualTo(new Contact(
            1,
            "test@email.com",
            "+380000000000",
            "password"
        ));
    }

    @Test
    @DisplayName("Contact was deleted")
    void contact_was_deleted() {
        contactService.delete(1);
        verify(contactRepository).delete(1);
    }

    @Test
    @DisplayName("Basket was updated")
    void basket_was_updated() {
        when(contactRepository.save(Contact.of(
            "test@email.com",
            "+380000000000",
            "password"
        ).withId(1), 1))
            .thenReturn(Contact.of(
                "test@email.com",
                "+380000000000",
                "password"
            ).withId(1));

        Contact updatedContact = contactService.update(1, Contact.of(
            "test@email.com",
            "+381111111111",
            "password"
        ).withId(1));

        assertThat(updatedContact).isEqualTo(new Contact(
            1,
            "test@email.com",
            "+381111111111",
            "password"
        ));
    }
}
