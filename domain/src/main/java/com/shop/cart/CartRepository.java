package com.shop.cart;

import com.shop.interfaces.RepositoryInterface;
import com.shop.user.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CartRepository implements RepositoryInterface<Cart> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CartRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Cart> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM cart",
            new BeanPropertyRowMapper<>(Cart.class)
        );
    }

    @Override
    public Optional<Cart> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM cart WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Cart.class)
            )
            .stream().findAny();
    }

    @Override
    public void save(Cart cart) {
        jdbcTemplate.update(
            "INSERT INTO cart (total_cost, user_id) VALUES (:total_cost, :user_id)",
            Map.ofEntries(
                Map.entry("total_cost", cart.getTotalCost()),
                Map.entry("user_id", cart.getUserId())
            )
        );
    }

    @Override
    public void update(Cart cart) {
        jdbcTemplate.update(
            "UPDATE cart SET total_cost=:total_cost WHERE id=:id",
            Map.ofEntries(
                Map.entry("total_cost", cart.getTotalCost()),
                Map.entry("id", cart.getId())
            )
        );
    }

    @Override
    public void delete(Cart cart) {
        jdbcTemplate.update(
            "DELETE FROM cart WHERE id=:id",
            Map.ofEntries(Map.entry("id", cart.getId()))
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
