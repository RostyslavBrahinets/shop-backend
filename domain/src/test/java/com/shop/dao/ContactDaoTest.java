package com.shop.dao;

import com.shop.configs.DatabaseConfig;
import com.shop.models.Contact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/contact/V20220421161842__Create_table_contact.sql"
})
public class ContactDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ContactDao contactDao;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(jdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        contactDao = new ContactDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "contact", "person"
        );
    }

    private int fetchContactsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "contact"
        );
    }

    @Test
    @DisplayName("Save contact")
    void save_contact() {
        contactDao.save(
            "test@email.com",
            "+380000000000",
            "password",
            1
        );

        var contactsCount = fetchContactsCount();

        assertThat(contactsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple contacts")
    void save_multiple_contacts() {
        contactDao.save(
            "test1@email.com",
            "+380000000000",
            "password",
            1
        );
        contactDao.save(
            "test2@email.com",
            "+380000000001",
            "password",
            2
        );

        var contactsCount = fetchContactsCount();

        assertThat(contactsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Contact by id was not found")
    void contact_by_id_was_not_found() {
        Optional<Contact> contact = contactDao.findById(1);

        assertThat(contact).isEmpty();
    }

    @Test
    @DisplayName("Contact by id was found")
    void contact_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("contact")
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "phone", "password", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("person_id", 1)
                )
            );

        Optional<Contact> contact = contactDao.findById(1);

        assertThat(contact).get().isEqualTo(
            Contact.of(
                    "test@email.com",
                    "+380000000000",
                    "password"
                )
                .withId(1));
    }

    @Test
    @DisplayName("Contact by person was not found")
    void contact_by_person_was_not_found() {
        Optional<Contact> contact = contactDao.findByPerson(1);

        assertThat(contact).isEmpty();
    }

    @Test
    @DisplayName("Contact by person was found")
    void contact_by_person_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("contact")
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "phone", "password", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("person_id", 1)
                )
            );

        Optional<Contact> contact = contactDao.findByPerson(1);

        assertThat(contact).get().isEqualTo(
            Contact.of(
                    "test@email.com",
                    "+380000000000",
                    "password"
                )
                .withId(1));
    }

    @Test
    @DisplayName("Find all baskets")
    void find_all_baskets() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("email", "test1@email.com"),
                Map.entry("phone", "+380000000000"),
                Map.entry("password", "password"),
                Map.entry("person_id", 1)
            ),
            Map.ofEntries(
                Map.entry("email", "test2@email.com"),
                Map.entry("phone", "+380000000001"),
                Map.entry("password", "password"),
                Map.entry("person_id", 2)
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("contact")
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "phone", "password", "person_id")
            .executeBatch(batchInsertParameters);

        List<Contact> baskets = contactDao.findAll();

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

    @Test
    @DisplayName("Contact not deleted in case when not exists")
    void contact_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> contactDao.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Contact deleted")
    void contact_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("contact")
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "phone", "password", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("person_id", 1)
                )
            );

        var contactsCountBeforeDeletion = fetchContactsCount();

        assertThat(contactsCountBeforeDeletion).isEqualTo(1);

        contactDao.delete(1);

        var contactsCount = fetchContactsCount();

        assertThat(contactsCount).isZero();
    }

    @Test
    @DisplayName("Contact updated")
    void contact_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("contact")
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "phone", "password", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("email", "test@email.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("person_id", 1)
                )
            );

        contactDao.update(1, "+380111111111");

        var updatedContact = jdbcTemplate.queryForObject(
            "SELECT phone FROM contact WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            String.class
        );

        assertThat(updatedContact).isEqualTo("+380111111111");
    }
}
