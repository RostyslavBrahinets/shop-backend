package com.shop.category;

import com.shop.category.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CategoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM category",
            new BeanPropertyRowMapper<>(Category.class)
        );
    }

    public Optional<Category> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    public Optional<Category> findByName(String name) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE name=:name",
                Map.ofEntries(Map.entry("name", name)),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    public void save(Category category) {
        jdbcTemplate.update(
            "INSERT INTO category (name) VALUES (:name)",
            Map.ofEntries(Map.entry("name", category.getName()))
        );
    }

    public void delete(Category category) {
        jdbcTemplate.update(
            "DELETE FROM category WHERE name=:name",
            Map.ofEntries(Map.entry("name", category.getName()))
        );
    }
}
