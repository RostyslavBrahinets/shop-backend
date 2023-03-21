package com.shop.user;

import com.shop.interfaces.RepositoryInterface;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository implements RepositoryInterface<User> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM \"user\"",
            new BeanPropertyRowMapper<>(User.class)
        );
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM \"user\" WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(User.class)
            )
            .stream().findAny();
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
            "INSERT INTO \"user\" (first_name, last_name, email, phone, password, admin_number_id)"
                + "VALUES (:first_name, :last_name, :email, :phone, :password, :admin_number_id)",
            Map.ofEntries(
                Map.entry("first_name", user.getFirstName()),
                Map.entry("last_name", user.getLastName()),
                Map.entry("email", user.getEmail()),
                Map.entry("phone", user.getPhone()),
                Map.entry("password", user.getPassword()),
                Map.entry("admin_number_id", user.getAdminNumberId())
            )
        );
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
            "UPDATE \"user\" SET first_name=:first_name, last_name=:last_name WHERE id=:id",
            Map.ofEntries(
                Map.entry("first_name", user.getFirstName()),
                Map.entry("last_name", user.getLastName()),
                Map.entry("id", user.getId())
            )
        );
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update(
            "DELETE FROM \"user\" WHERE id=:id",
            Map.ofEntries(Map.entry("id", user.getId()))
        );
    }

    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM \"user\" WHERE email=:email",
                Map.ofEntries(Map.entry("email", email)),
                new BeanPropertyRowMapper<>(User.class)
            )
            .stream().findAny();
    }
}
