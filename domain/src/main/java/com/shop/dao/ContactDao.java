package com.shop.dao;

import com.shop.models.Contact;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ContactDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ContactDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contact> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM contact",
            new BeanPropertyRowMapper<>(Contact.class)
        );
    }

    public void save(
        String email,
        String phone,
        String password,
        long personId
    ) {
        jdbcTemplate.update(
            "INSERT INTO contact (email, phone, password, person_id) "
                + "VALUES (:email, :phone, :password, :person_id)",
            Map.ofEntries(
                Map.entry("email", email),
                Map.entry("phone", phone),
                Map.entry("password", password),
                Map.entry("person_id", personId)
            )
        );
    }

    public void update(long id, String phone) {
        jdbcTemplate.update(
            "UPDATE contact SET phone=:phone WHERE person_id=:id",
            Map.ofEntries(
                Map.entry("phone", phone),
                Map.entry("id", id)
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM contact WHERE id=:id",
            Map.ofEntries(Map.entry("id", id))
        );
    }

    public Optional<Contact> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM contact WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Contact.class)
            )
            .stream().findAny();
    }

    public Optional<Contact> findByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM contact WHERE person_id=:person_id",
                Map.ofEntries(Map.entry("person_id", personId)),
                new BeanPropertyRowMapper<>(Contact.class)
            )
            .stream().findAny();
    }
}
