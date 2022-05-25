package com.shop.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.ContactDao;
import com.shop.dao.PersonDao;
import com.shop.models.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    ContactRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/contact/V20220421161842__Create_table_contact.sql"
})
public class ContactRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(namedParameterJdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE contact");
    }

    @Test
    @DisplayName("No contacts in database")
    void no_history_records_in_db() {
        int contactsCount = contactRepository.count();

        assertThat(contactsCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing contact")
    void nothing_happened_when_trying_to_delete_not_existing_contact() {
        assertThatCode(() -> contactRepository.delete(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Contact was deleted")
    @DirtiesContext
    void contact_was_deleted() {
        var contactToSave = Contact.of(
            "test@email.com",
            "+380000000000",
            "password"
        );

        contactRepository.save(contactToSave, 1);

        assertThat(contactRepository.count()).isEqualTo(1);

        contactRepository.delete(contactRepository.count());

        assertThat(contactRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save contact and check contact data")
    @DirtiesContext
    void save_contact_and_check_contact_data() {
        var contactToSave = Contact.of(
            "test@email.com",
            "+380000000000",
            "password"
        );

        contactRepository.save(contactToSave, 1);
        var savedContact = contactRepository.findById(contactRepository.count());
        Contact contact = null;
        if (savedContact.isPresent()) {
            contact = savedContact.get();
        }

        assertThat(contact).extracting(Contact::getId).isEqualTo(1L);
        assertThat(contact).extracting(Contact::getEmail).isEqualTo("test@email.com");
        assertThat(contact).extracting(Contact::getPhone).isEqualTo("+380000000000");
        assertThat(contact).extracting(Contact::getPassword).isEqualTo("password");
    }

    @Test
    @DisplayName("Save multiple contacts")
    @DirtiesContext
    void save_multiple_contacts() {
        contactRepository.save(
            Contact.of(
                "test1@email.com",
                "+380000000000",
                "password"
            ),
            1
        );
        contactRepository.save(
            Contact.of(
                "test2@email.com",
                "+380000000001",
                "password"
            ),
            2
        );

        assertThat(contactRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Contact was not found")
    void contact_was_not_found() {
        Optional<Contact> contact = contactRepository.findById(1);

        assertThat(contact).isEmpty();
    }

    @Test
    @DisplayName("Contact was found")
    @DirtiesContext
    void contact_was_found() {
        contactRepository.save(
            Contact.of(
                "test@email.com",
                "+380000000000",
                "password"
            ),
            1
        );

        Optional<Contact> contact = contactRepository.findById(contactRepository.count());

        assertThat(contact).get().isEqualTo(Contact.of(
                    "test@email.com",
                    "+380000000000",
                    "password"
                )
                .withId(1)
        );
    }

    @Test
    @DisplayName("Find all contacts")
    @DirtiesContext
    void find_all_contacts() {
        contactRepository.save(
            Contact.of(
                "test1@email.com",
                "+380000000000",
                "password"
            ),
            1
        );
        contactRepository.save(
            Contact.of(
                "test2@email.com",
                "+380000000001",
                "password"
            ),
            2
        );

        var baskets = contactRepository.findAll();

        assertThat(baskets).isEqualTo(
            List.of(
                Contact.of(
                        "test1@email.com",
                        "+380000000000",
                        "password"
                    )
                    .withId(1),
                Contact.of(
                        "test2@email.com",
                        "+380000000001",
                        "password"
                    )
                    .withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public ContactDao contactDao() {
            return new ContactDao(namedParameterJdbcTemplate);
        }

        @Bean
        public ContactRepository contactRepository(ContactDao contactDao) {
            return new ContactRepository(contactDao);
        }
    }
}
