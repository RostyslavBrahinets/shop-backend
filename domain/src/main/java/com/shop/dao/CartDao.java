package com.shop.dao;

import com.shop.models.Cart;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CartDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CartDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cart> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM cart",
            new BeanPropertyRowMapper<>(Cart.class)
        );
    }

    public Optional<Cart> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM cart WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Cart.class)
            )
            .stream().findAny();
    }

    public Optional<Cart> findByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM cart WHERE person_id=:person_id",
                Map.ofEntries(Map.entry("person_id", personId)),
                new BeanPropertyRowMapper<>(Cart.class)
            )
            .stream().findAny();
    }

    public void save(double totalCost, long personId) {
        jdbcTemplate.update(
            "INSERT INTO cart (total_cost, person_id) VALUES (:total_cost, :person_id)",
            Map.ofEntries(
                Map.entry("total_cost", totalCost),
                Map.entry("person_id", personId)
            )
        );
    }

    public void update(long id, double totalCost) {
        jdbcTemplate.update(
            "UPDATE cart SET total_cost=:total_cost WHERE id=:id",
            Map.ofEntries(
                Map.entry("total_cost", totalCost),
                Map.entry("id", id)
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM cart WHERE id=:id",
            Map.ofEntries(Map.entry("id", id))
        );
    }
}
