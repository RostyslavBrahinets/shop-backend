package com.shop.integration.dao;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.PersonDao;
import com.shop.models.Person;
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
public class PersonDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private PersonDao personDao;

    @BeforeEach
    void setUp() {
        personDao = new PersonDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "contact", "person"
        );
    }

    private int fetchPeopleCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "person"
        );
    }

    @Test
    @DisplayName("Save person")
    void save_person() {
        personDao.save("John", "Smith");

        var peopleCount = fetchPeopleCount();

        assertThat(peopleCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple people")
    void save_multiple_people() {
        personDao.save("John", "Smith");
        personDao.save("John", "Smith");

        var peopleCount = fetchPeopleCount();

        assertThat(peopleCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Person by id was not found")
    void person_by_id_was_not_found() {
        Optional<Person> person = personDao.findById(1);

        assertThat(person).isEmpty();
    }

    @Test
    @DisplayName("Person by id was found")
    void person_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person")
            .usingGeneratedKeyColumns("id")
            .usingColumns("first_name", "last_name")
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith")
                )
            );

        Optional<Person> person = personDao.findById(1);

        assertThat(person).get().isEqualTo(Person.of("John", "Smith").withId(1));
    }

    @Test
    @DisplayName("Person by email was not found")
    void person_by_email_was_not_found() {
        Optional<Person> person = personDao.findByEmail("test@gmail.com");

        assertThat(person).isEmpty();
    }

    @Test
    @DisplayName("Person by email was found")
    void person_by_email_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person")
            .usingGeneratedKeyColumns("id")
            .usingColumns("first_name", "last_name")
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith")
                )
            );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("contact")
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "phone", "password", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("email", "test@gmail.com"),
                    Map.entry("phone", "+380000000000"),
                    Map.entry("password", "password"),
                    Map.entry("person_id", 1)
                )
            );

        Optional<Person> person = personDao.findByEmail("test@gmail.com");

        assertThat(person).get().isEqualTo(Person.of("John", "Smith").withId(1));
    }

    @Test
    @DisplayName("Find all people")
    void find_all_people() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("first_name", "John"),
                Map.entry("last_name", "Smith")
            ),
            Map.ofEntries(
                Map.entry("first_name", "John"),
                Map.entry("last_name", "Smith")
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person")
            .usingGeneratedKeyColumns("id")
            .usingColumns("first_name", "last_name")
            .executeBatch(batchInsertParameters);

        List<Person> people = personDao.findAll();

        assertThat(people).isEqualTo(
            List.of(
                Person.of("John", "Smith").withId(1),
                Person.of("John", "Smith").withId(2)
            )
        );
    }

    @Test
    @DisplayName("Person not deleted in case when not exists")
    void person_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> personDao.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Person deleted")
    void person_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person")
            .usingGeneratedKeyColumns("id")
            .usingColumns("first_name", "last_name")
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith")
                )
            );

        var peopleCountBeforeDeletion = fetchPeopleCount();

        assertThat(peopleCountBeforeDeletion).isEqualTo(1);

        personDao.delete(1);

        var peopleCount = fetchPeopleCount();

        assertThat(peopleCount).isZero();
    }

    @Test
    @DisplayName("Person updated")
    void person_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("person")
            .usingGeneratedKeyColumns("id")
            .usingColumns("first_name", "last_name")
            .execute(
                Map.ofEntries(
                    Map.entry("first_name", "John"),
                    Map.entry("last_name", "Smith")
                )
            );

        personDao.update(1, "Alex", "Smith");

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT first_name FROM person WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            String.class
        );

        assertThat(updatedBasket).isEqualTo("Alex");
    }
}
