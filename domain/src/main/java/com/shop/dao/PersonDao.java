package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Person> getPeople() {
        return jdbcTemplate.query(
            "SELECT * FROM person",
            new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public void addPerson(Person person) {
        String sql = "INSERT INTO person (first_name, last_name, role)"
            + " VALUES (:first_name, :last_name, :role)";

        Map<String, Object> param = Map.of(
            "first_name", person.getFirstName(),
            "last_name", person.getLastName(),
            "role", person.getRole().toString()
        );

        jdbcTemplate.update(sql, param);
    }

    public void updatePerson(int id, Person updatedPerson) {
        String sql = "UPDATE person SET first_name=:first_name, last_name=:last_name, role=:role"
            + " WHERE id=:id";

        Map<String, Object> param = Map.of(
            "first_name", updatedPerson.getFirstName(),
            "last_name", updatedPerson.getLastName(),
            "role", updatedPerson.getRole().toString(),
            "id", id
        );

        jdbcTemplate.update(sql, param);
    }

    public void deletePerson(int id) {
        String sql = "DELETE FROM person WHERE id=:id";
        Map<String, Integer> param = Map.of("id", id);
        jdbcTemplate.update(sql, param);
    }

    public Optional<Person> getPerson(int id) {
        Map<String, Integer> param = Map.of("id", id);

        Person person = jdbcTemplate.query(
                "SELECT * FROM person WHERE id=:id",
                param,
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(person);
    }
}
