package com.shop.cart;

import com.shop.user.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CartRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CartRepository(NamedParameterJdbcTemplate jdbcTemplate) {
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

    public Optional<Cart> findByUser(long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM cart WHERE user_id=:user_id",
                Map.ofEntries(Map.entry("user_id", userId)),
                new BeanPropertyRowMapper<>(Cart.class)
            )
            .stream().findAny();
    }

    public void save(double totalCost, long userId) {
        jdbcTemplate.update(
            "INSERT INTO cart (total_cost, user_id) VALUES (:total_cost, :user_id)",
            Map.ofEntries(
                Map.entry("total_cost", totalCost),
                Map.entry("user_id", userId)
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

    public Optional<Cart> findByUser(User user) {
        return jdbcTemplate.query(
                "SELECT * FROM cart WHERE user_id=:user_id",
                Map.ofEntries(Map.entry("user_id", user.getId())),
                new BeanPropertyRowMapper<>(Cart.class)
            )
            .stream().findAny();
    }
}
