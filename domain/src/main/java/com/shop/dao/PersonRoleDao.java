package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Person;
import com.shop.models.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PersonRoleDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();
    public List<Person> getPeople(long roleId) {
        Map<String, Long> param = Map.of("role_id", roleId);

        return jdbcTemplate.query(
                "SELECT * FROM person p, person_role pr "
                    + "WHERE pr.person_id=p.id and pr.role_id=:role_id",
                param,
                new BeanPropertyRowMapper<>(Person.class)
            );
    }


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

    public void addRoleForPerson(long personId, long roleId) {
        String sql = "INSERT INTO person_role (person_id, role_id)"
            + " VALUES (:person_id, :role_id)";

        Map<String, Long> param = Map.of(
            "person_id", personId,
            "role_id", roleId
        );

        jdbcTemplate.update(sql, param);
    }
}
