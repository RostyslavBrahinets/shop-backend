package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Optional;

public class RoleDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public Optional<Role> getRole(long personId) {
        Map<String, Long> param = Map.of("person_id", personId);

        Role role = jdbcTemplate.query(
                "SELECT * FROM role r, person_role pr "
                    + "WHERE pr.role_id=r.id and pr.person_id=:person_id",
                param,
                new BeanPropertyRowMapper<>(Role.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(role);
    }
}
