package com.shop.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.PersonDao;
import com.shop.models.Person;
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
    PersonRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql"
})
public class PersonRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE person");
    }

    @Test
    @DisplayName("No people in database")
    void no_history_records_in_db() {
        int peopleCount = personRepository.count();

        assertThat(peopleCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing person")
    void nothing_happened_when_trying_to_delete_not_existing_person() {
        assertThatCode(() -> personRepository.delete(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Person was deleted")
    @DirtiesContext
    void person_was_deleted() {
        var personToSave = Person.of("John", "Smith");

        personRepository.save(personToSave);

        assertThat(personRepository.count()).isEqualTo(1);

        personRepository.delete(personRepository.count());

        assertThat(personRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save person and check person data")
    @DirtiesContext
    void save_person_and_check_person_data() {
        var personToSave = Person.of("John", "Smith");
        personRepository.save(personToSave);
        var savedPerson = personRepository.findById(personRepository.count());
        Person person = null;
        if (savedPerson.isPresent()) {
            person = savedPerson.get();
        }

        assertThat(person).extracting(Person::getId).isEqualTo(1L);
        assertThat(person).extracting(Person::getFirstName).isEqualTo("John");
        assertThat(person).extracting(Person::getLastName).isEqualTo("Smith");
    }

    @Test
    @DisplayName("Save multiple people")
    @DirtiesContext
    void save_multiple_people() {
        personRepository.save(Person.of("John", "Smith"));
        personRepository.save(Person.of("John", "Smith"));

        assertThat(personRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Person was not found")
    void person_was_not_found() {
        Optional<Person> person = personRepository.findById(1);

        assertThat(person).isEmpty();
    }

    @Test
    @DisplayName("Person was found")
    @DirtiesContext
    void person_was_found() {
        personRepository.save(Person.of("John", "Smith"));

        Optional<Person> person = personRepository.findById(personRepository.count());

        assertThat(person).get().isEqualTo(Person.of("John", "Smith").withId(1));
    }

    @Test
    @DisplayName("Find all people")
    @DirtiesContext
    void find_all_people() {
        personRepository.save(Person.of("John", "Smith"));
        personRepository.save(Person.of("John", "Smith"));

        var people = personRepository.findAll();

        assertThat(people).isEqualTo(
            List.of(
                Person.of("John", "Smith").withId(1),
                Person.of("John", "Smith").withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public PersonDao personDao() {
            return new PersonDao(namedParameterJdbcTemplate);
        }

        @Bean
        public PersonRepository personRepository(PersonDao personDao) {
            return new PersonRepository(personDao);
        }
    }
}
