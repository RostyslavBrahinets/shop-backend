package com.shop.dao;

import com.shop.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PersonDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PersonDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getPeople() {
        return jdbcTemplate.query(
            "SELECT * FROM person",
            new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public void addPerson(Person person) {
        jdbcTemplate.update(
            "INSERT INTO person (first_name, last_name) VALUES (:first_name, :last_name)",
            Map.of(
                "first_name", person.getFirstName(),
                "last_name", person.getLastName()
            )
        );
    }

    public void deletePerson(long id) {
        jdbcTemplate.update(
            "DELETE FROM person WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Person> getPerson(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM person WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny();
    }

    public Optional<Person> getPerson(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM person p, contact c "
                    + "WHERE p.id=c.person_id and c.email=:email",
                Map.of("email", email),
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny();
    }

    public void updatePerson(long id, Person updatedPerson) {
        jdbcTemplate.update(
            "UPDATE person SET first_name=:first_name, last_name=:last_name WHERE id=:id",
            Map.of(
                "first_name", updatedPerson.getFirstName(),
                "last_name", updatedPerson.getLastName(),
                "id", id
            )
        );
    }
}
