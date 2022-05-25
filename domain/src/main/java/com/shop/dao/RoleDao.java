package com.shop.dao;

import com.shop.models.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class RoleDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoleDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Role> findByName(String name) {
        return jdbcTemplate.query(
                "SELECT * FROM role WHERE name=:name",
                Map.ofEntries(Map.entry("name", name)),
                new BeanPropertyRowMapper<>(Role.class)
            )
            .stream().findAny();
    }
}
