package com.shop.dao;

import com.shop.models.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class UserRoleDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRoleDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Role> findRoleForUser(long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM role r, user_role ur "
                    + "WHERE ur.role_id=r.id and ur.user_id=:user_id",
                Map.ofEntries(Map.entry("user_id", userId)),
                new BeanPropertyRowMapper<>(Role.class)
            )
            .stream().findAny();
    }

    public void saveRoleForUser(long userId, long roleId) {
        jdbcTemplate.update(
            "INSERT INTO user_role (user_id, role_id) VALUES (:user_id, :role_id)",
            Map.ofEntries(
                Map.entry("user_id", userId),
                Map.entry("role_id", roleId)
            )
        );
    }

    public void updateRoleForUser(long userId, long roleId) {
        jdbcTemplate.update(
            "UPDATE user_role SET role_id=:role_id WHERE user_id=:user_id",
            Map.ofEntries(
                Map.entry("user_id", userId),
                Map.entry("role_id", roleId)
            )
        );
    }
}
