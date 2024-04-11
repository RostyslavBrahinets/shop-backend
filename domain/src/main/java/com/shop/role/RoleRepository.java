package com.shop.role;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class RoleRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
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
