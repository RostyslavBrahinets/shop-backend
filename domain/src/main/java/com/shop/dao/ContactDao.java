package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Contact;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ContactDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Contact> getContacts() {
        return jdbcTemplate.query(
            "SELECT * FROM contact",
            new BeanPropertyRowMapper<>(Contact.class)
        );
    }

    public void addContact(Contact contact, long personId) {
        jdbcTemplate.update(
            "INSERT INTO contact (email, phone, password, person_id) "
                + "VALUES (:email, :phone, :password, :person_id)",
            Map.of(
                "email", contact.getEmail(),
                "phone", contact.getPhone(),
                "password", contact.getPassword(),
                "person_id", personId
            )
        );
    }

    public void updateContact(long id, Contact updatedContact) {
        jdbcTemplate.update(
            "UPDATE contact SET email=:email, phone=:phone WHERE id=:id",
            Map.of(
                "email", updatedContact.getEmail(),
                "phone", updatedContact.getPhone(),
                "id", id
            ));
    }

    public void deleteContact(long id) {
        jdbcTemplate.update(
            "DELETE FROM contact WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Contact> getContact(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM contact WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Contact.class)
            )
            .stream().findAny();
    }

    public Optional<Contact> getContactByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM contact WHERE person_id=:person_id",
                Map.of("person_id", personId),
                new BeanPropertyRowMapper<>(Contact.class)
            )
            .stream().findAny();
    }
}
