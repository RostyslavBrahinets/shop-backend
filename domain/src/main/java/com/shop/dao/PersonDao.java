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

    public List<Person> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM person",
            new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public void save(String firstName, String lastName) {
        jdbcTemplate.update(
            "INSERT INTO person (first_name, last_name) VALUES (:first_name, :last_name)",
            Map.ofEntries(
                Map.entry("first_name", firstName),
                Map.entry("last_name", lastName)
            )
        );
    }

    public void update(long id, String firstName, String lastName) {
        jdbcTemplate.update(
            "UPDATE person SET first_name=:first_name, last_name=:last_name WHERE id=:id",
            Map.ofEntries(
                Map.entry("first_name", firstName),
                Map.entry("last_name", lastName),
                Map.entry("id", id)
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM person WHERE id=:id",
            Map.ofEntries(Map.entry("id", id))
        );
    }

    public Optional<Person> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM person WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny();
    }

    public Optional<Person> findByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM person p, contact c "
                    + "WHERE p.id=c.person_id and c.email=:email",
                Map.ofEntries(Map.entry("email", email)),
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny();
    }
}
