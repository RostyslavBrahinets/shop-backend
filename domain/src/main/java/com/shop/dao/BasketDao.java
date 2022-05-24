package com.shop.dao;

import com.shop.models.Basket;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BasketDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BasketDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Basket> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM basket",
            new BeanPropertyRowMapper<>(Basket.class)
        );
    }

    public void save(double totalCost, long personId) {
        jdbcTemplate.update(
            "INSERT INTO basket (total_cost, person_id) VALUES (:total_cost, :person_id)",
            Map.of(
                "total_cost", totalCost,
                "person_id", personId
            )
        );
    }

    public void update(long id, double totalCost) {
        jdbcTemplate.update(
            "UPDATE basket SET total_cost=:total_cost WHERE id=:id",
            Map.of(
                "total_cost", totalCost,
                "id", id
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM basket WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Basket> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM basket WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Basket.class)
            )
            .stream().findAny();
    }

    public Optional<Basket> findByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM basket WHERE person_id=:person_id",
                Map.of("person_id", personId),
                new BeanPropertyRowMapper<>(Basket.class)
            )
            .stream().findAny();
    }
}
