package com.shop.dao;

import com.shop.models.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM \"user\"",
            new BeanPropertyRowMapper<>(User.class)
        );
    }

    public Optional<User> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM \"user\" WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(User.class)
            )
            .stream().findAny();
    }

    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM \"user\" WHERE email=:email",
                Map.ofEntries(Map.entry("email", email)),
                new BeanPropertyRowMapper<>(User.class)
            )
            .stream().findAny();
    }

    public void save(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        long adminNumberId
    ) {
        jdbcTemplate.update(
            "INSERT INTO \"user\" (first_name, last_name, email, phone, password, admin_number_id)"
                + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number_id)",
            Map.ofEntries(
                Map.entry("first_name", firstName),
                Map.entry("last_name", lastName),
                Map.entry("email", email),
                Map.entry("phone", phone),
                Map.entry("password", password),
                Map.entry("admin_number_id", adminNumberId)
            )
        );
    }

    public void update(long id, String firstName, String lastName) {
        jdbcTemplate.update(
            "UPDATE \"user\" SET first_name=:first_name, last_name=:last_name WHERE id=:id",
            Map.ofEntries(
                Map.entry("first_name", firstName),
                Map.entry("last_name", lastName),
                Map.entry("id", id)
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM \"user\" WHERE id=:id",
            Map.ofEntries(Map.entry("id", id))
        );
    }
}
