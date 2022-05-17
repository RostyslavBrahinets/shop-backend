package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CategoryDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Category> getCategories() {
        return jdbcTemplate.query(
            "SELECT * FROM category",
            new BeanPropertyRowMapper<>(Category.class)
        );
    }

    public void addCategory(Category category) {
        jdbcTemplate.update(
            "INSERT INTO category (name) VALUES (:name)",
            Map.of(
                "name", category.getName()
            )
        );
    }

    public void deleteCategory(long id) {
        jdbcTemplate.update(
            "DELETE FROM person WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Category> getCategory(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    public Optional<Category> getCategory(String name) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE name=:name",
                Map.of("name", name),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }
}
