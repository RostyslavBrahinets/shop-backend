package com.shop.dao;

import com.shop.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductsCartsDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductsCartsDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAllProductsInCart(long cartId) {
        return jdbcTemplate.query(
            "SELECT * FROM product p, products_carts pb "
                + "WHERE pb.product_id=p.id AND pb.cart_id=:cart_id",
            Map.ofEntries(Map.entry("cart_id", cartId)),
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public void saveProductToCart(long productId, long cartId) {
        jdbcTemplate.update(
            "INSERT INTO products_carts (product_id, cart_id)"
                + " VALUES (:product_id, :cart_id)",
            Map.ofEntries(
                Map.entry("product_id", productId),
                Map.entry("cart_id", cartId)
            )
        );
    }

    public void deleteProductFromCart(long productId, long cartId) {
        jdbcTemplate.update(
            "DELETE FROM products_carts "
                + "WHERE ctid IN "
                + "(SELECT ctid FROM products_carts "
                + "WHERE product_id=:product_id AND cart_id=:cart_id "
                + "ORDER BY product_id LIMIT 1)",
            Map.ofEntries(
                Map.entry("product_id", productId),
                Map.entry("cart_id", cartId)
            )
        );
    }

    public void deleteProductsFromCart(long cartId) {
        jdbcTemplate.update(
            "DELETE FROM products_carts WHERE cart_id=:cart_id",
            Map.ofEntries(Map.entry("cart_id", cartId))
        );
    }
}
