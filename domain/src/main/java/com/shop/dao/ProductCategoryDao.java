package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ProductCategoryDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public Optional<Category> getCategoryForProduct(long productId) {
        return jdbcTemplate.query(
                "SELECT * FROM category c, product_category pc "
                    + "WHERE pc.category_id = c.id AND pc.product_id=:product_id",
                Map.of("product_id", productId),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    public void addProductToCategory(long productId, long categoryId) {
        jdbcTemplate.update(
            "INSERT INTO product_category (product_id, category_id)"
                + " VALUES (:product_id, :category_id)",
            Map.of(
                "product_id", productId,
                "category_id", categoryId
            )
        );
    }
}
