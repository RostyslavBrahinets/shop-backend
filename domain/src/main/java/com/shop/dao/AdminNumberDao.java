package com.shop.dao;

import com.shop.models.AdminNumber;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AdminNumberDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AdminNumberDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AdminNumber> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM admin_number",
            new BeanPropertyRowMapper<>(AdminNumber.class)
        );
    }

    public Optional<AdminNumber> findByNumber(String number) {
        return jdbcTemplate.query(
                "SELECT * FROM admin_number WHERE number=:number",
                Map.ofEntries(Map.entry("number", number)),
                new BeanPropertyRowMapper<>(AdminNumber.class)
            )
            .stream().findAny();
    }

    public void save(String number) {
        jdbcTemplate.update(
            "INSERT INTO admin_number (number) VALUES (:number)",
            Map.ofEntries(Map.entry("number", number))
        );
    }
}
