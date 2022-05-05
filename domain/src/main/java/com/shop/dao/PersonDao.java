package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PersonDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Person> getPeople() {
        return jdbcTemplate.query(
            "SELECT * FROM person",
            new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public void addPerson(Person person) {
        String sql = "INSERT INTO person (first_name, last_name)"
            + " VALUES (:first_name, :last_name)";

        Map<String, Object> param = Map.of(
            "first_name", person.getFirstName(),
            "last_name", person.getLastName()
        );

        jdbcTemplate.update(sql, param);
    }

    public void updatePerson(long id, Person updatedPerson) {
        String sql = "UPDATE person SET first_name=:first_name, last_name=:last_name"
            + " WHERE id=:id";

        Map<String, Object> param = Map.of(
            "first_name", updatedPerson.getFirstName(),
            "last_name", updatedPerson.getLastName(),
            "id", id
        );

        jdbcTemplate.update(sql, param);
    }

    public void deletePerson(long id) {
        String sql = "DELETE FROM person WHERE id=:id";
        Map<String, Long> param = Map.of("id", id);
        jdbcTemplate.update(sql, param);
    }

    public Optional<Person> getPerson(long id) {
        Map<String, Long> param = Map.of("id", id);

        Person person = jdbcTemplate.query(
                "SELECT * FROM person WHERE id=:id",
                param,
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(person);
    }

    public Optional<Person> getPersonByEmail(String email) {
        Map<String, String> param = Map.of("email", email);

        Person person = jdbcTemplate.query(
                "SELECT * FROM person p, contact c "
                    + "WHERE p.id=c.person_id and c.email=:email",
                param,
                new BeanPropertyRowMapper<>(Person.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(person);
    }
}
