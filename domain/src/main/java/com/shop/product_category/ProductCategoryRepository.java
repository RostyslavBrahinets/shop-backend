package com.shop.product_category;

import com.shop.category.Category;
import com.shop.product.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductCategoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductCategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAllProductsInCategory(long categoryId) {
        return jdbcTemplate.query(
            "SELECT p.* FROM product p, product_category pc "
                + "WHERE pc.product_id=p.id AND pc.category_id=:category_id",
            Map.ofEntries(Map.entry("category_id", categoryId)),
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public Optional<Category> findCategoryForProduct(long productId) {
        return jdbcTemplate.query(
                "SELECT c.* FROM category c, product_category pc "
                    + "WHERE pc.category_id=c.id AND pc.product_id=:product_id",
                Map.ofEntries(Map.entry("product_id", productId)),
                new BeanPropertyRowMapper<>(Category.class)
            )
            .stream().findAny();
    }

    public void saveProductToCategory(long productId, long categoryId) {
        jdbcTemplate.update(
            "INSERT INTO product_category (product_id, category_id)"
                + " VALUES (:product_id, :category_id)",
            Map.ofEntries(
                Map.entry("product_id", productId),
                Map.entry("category_id", categoryId)
            )
        );
    }

    public void deleteProductFromCategory(long productId, long categoryId) {
        jdbcTemplate.update(
            "DELETE FROM product_category "
                + "WHERE product_id=:product_id AND category_id=:category_id",
            Map.ofEntries(
                Map.entry("product_id", productId),
                Map.entry("category_id", categoryId)
            )
        );
    }
}
