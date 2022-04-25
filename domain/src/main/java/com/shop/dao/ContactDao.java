package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Contact;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContactDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Contact> getContacts() {
        return jdbcTemplate.query(
            "SELECT * FROM contact",
            new BeanPropertyRowMapper<>(Contact.class)
        );
    }

    public void addContact(Contact contact, int personId) {
        String sql = "INSERT INTO contact (email, phone, person_id)"
            + " VALUES (:email, :phone, :person_id)";

        Map<String, Object> param = Map.of(
            "email", contact.getEmail(),
            "phone", contact.getPhone(),
            "person_id", personId
        );

        jdbcTemplate.update(sql, param);
    }

    public void updateContact(int id, Contact updatedContact) {
        String sql = "UPDATE contact SET email=:email, phone=:phone WHERE id=?";

        Map<String, Object> param = Map.of(
            "email", updatedContact.getEmail(),
            "phone", updatedContact.getPhone()
        );

        jdbcTemplate.update(sql, param);
    }

    public void deleteContact(int id) {
        String sql = "DELETE FROM contact WHERE id=:id";
        Map<String, Integer> param = Map.of("id", id);
        jdbcTemplate.update(sql, param);
    }

    public Optional<Contact> getContact(int id) {
        Map<String, Integer> param = Map.of("id", id);

        Contact contact = jdbcTemplate.query(
                "SELECT * FROM contact WHERE id=:id",
                param,
                new BeanPropertyRowMapper<>(Contact.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(contact);
    }
}