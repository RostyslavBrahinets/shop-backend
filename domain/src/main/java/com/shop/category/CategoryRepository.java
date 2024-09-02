package com.shop.category;

import com.shop.interfaces.RepositoryInterface;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CategoryRepository implements RepositoryInterface<Category> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM category",
            new BeanPropertyRowMapper<>(Category.class)
        );
    }

    @Override
    public Optional<Category> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    @Override
    public void save(Category category) {
        jdbcTemplate.update(
            "INSERT INTO category (name) VALUES (:name)",
            Map.ofEntries(Map.entry("name", category.getName()))
        );
    }

    @Override
    public Optional<Category> update(long id, Category category) {
        int updatedCategoryId = jdbcTemplate.update(
            "UPDATE category SET name=:name WHERE id=:id",
            Map.ofEntries(
                Map.entry("name", category.getName()),
                Map.entry("id", id)
            )
        );

        return findById(updatedCategoryId);
    }

    @Override
    public void delete(Category category) {
        jdbcTemplate.update(
            "DELETE FROM category WHERE name=:name",
            Map.ofEntries(Map.entry("name", category.getName()))
        );
    }

    public Optional<Category> findByName(String name) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE name=:name",
                Map.ofEntries(Map.entry("name", name)),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    public List<Category> findAllByNameLike(String filter) {
        return jdbcTemplate.query(
            "SELECT * FROM category c WHERE c.name ILIKE :filter",
            Map.ofEntries(Map.entry("filter", filter)),
            new BeanPropertyRowMapper<>(Category.class)
        );
    }
}
