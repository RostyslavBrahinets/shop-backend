package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Basket;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BasketDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Basket> getBaskets() {
        return jdbcTemplate.query(
            "SELECT * FROM basket",
            new BeanPropertyRowMapper<>(Basket.class)
        );
    }

    public void addBasket(Basket basket, long personId) {
        jdbcTemplate.update(
            "INSERT INTO basket (total_cost, person_id) VALUES (:total_cost, :person_id)",
            Map.of(
                "total_cost", basket.getTotalCost(),
                "person_id", personId
            )
        );
    }

    public void updateBasket(long id, Basket updatedBasket) {
        jdbcTemplate.update(
            "UPDATE basket SET total_cost=:total_cost WHERE id=:id",
            Map.of(
                "total_cost", updatedBasket.getTotalCost(),
                "id", id
            )
        );
    }

    public void deleteBasket(long id) {
        jdbcTemplate.update(
            "DELETE FROM contact WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Basket> getBasket(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM basket WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Basket.class)
            )
            .stream().findAny();
    }

    public Optional<Basket> getBasketByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM basket WHERE person_id=:person_id",
                Map.of("person_id", personId),
                new BeanPropertyRowMapper<>(Basket.class)
            )
            .stream().findAny();
    }
}
