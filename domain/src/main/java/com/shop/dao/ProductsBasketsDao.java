package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductsBasketsDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Product> getProductsFromBasket(long basketId) {
        return jdbcTemplate.query(
            "SELECT * FROM product p, products_baskets pb "
                + "WHERE pb.product_id = p.id AND pb.basket_id=:basket_id",
            Map.of("basket_id", basketId),
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public void addProductToBasket(long productId, long basketId) {
        jdbcTemplate.update(
            "INSERT INTO products_baskets (product_id, basket_id)"
                + " VALUES (:product_id, :basket_id)",
            Map.of(
                "product_id", productId,
                "basket_id", basketId
            )
        );
    }

    public void deleteProductFromBasket(long productId, long basketId) {
        jdbcTemplate.update(
            "DELETE FROM products_baskets "
                + "WHERE ctid IN "
                + "(SELECT ctid FROM products_baskets "
                + "WHERE product_id=:product_id AND basket_id=:basket_id "
                + "ORDER BY product_id LIMIT 1)",
            Map.of(
                "product_id", productId,
                "basket_id", basketId
            )
        );
    }

    public void deleteProductsFromBasket(long basketId) {
        jdbcTemplate.update(
            "DELETE FROM products_baskets WHERE basket_id=:basket_id",
            Map.of("basket_id", basketId)
        );
    }
}
