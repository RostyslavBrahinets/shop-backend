package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Product> getProducts() {
        return jdbcTemplate.query(
            "SELECT * FROM product",
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO product (name, describe, price, category, in_stock, image)"
            + " VALUES (:name, :describe, :price, :category, :in_stock, :image)";

        Map<String, Object> param = Map.of(
            "name", product.getName(),
            "describe", product.getDescribe(),
            "price", product.getPrice(),
            "category", product.getCategory().toString(),
            "in_stock", product.isInStock(),
            "image", product.getImage()
        );

        jdbcTemplate.update(sql, param);
    }

    public void updateProduct(long id, Product updatedProduct) {
        String sql = "UPDATE product SET name=:name, describe=:describe, price=:price, "
            + "category=:category, in_stock=:in_stock, image=:image "
            + "WHERE id=:id";

        Map<String, Object> param = Map.of(
            "name", updatedProduct.getName(),
            "describe", updatedProduct.getDescribe(),
            "price", updatedProduct.getPrice(),
            "category", updatedProduct.getCategory().toString(),
            "in_stock", updatedProduct.isInStock(),
            "image", updatedProduct.getImage(),
            "id", id
        );

        jdbcTemplate.update(sql, param);
    }

    public void deleteProduct(long id) {
        String sql = "DELETE FROM product WHERE id=:id";
        Map<String, Long> param = Map.of("id", id);
        jdbcTemplate.update(sql, param);
    }

    public Optional<Product> getProduct(long id) {
        Map<String, Long> param = Map.of("id", id);

        Product product = jdbcTemplate.query(
                "SELECT * FROM product WHERE id=:id",
                param,
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(product);
    }
}
