package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductsBasketsDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Product> getProductsFromBasket(long basketId) {
        Map<String, Long> param = Map.of(
            "basket_id", basketId
        );

        return jdbcTemplate.query(
            "SELECT * FROM product p, products_baskets pb "
                + "WHERE pb.product_id = p.id AND pb.basket_id=:basket_id",
            param,
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public void addProductToBasket(long productId, long basketId) {
        String sql = "INSERT INTO products_baskets (product_id, basket_id)"
            + " VALUES (:product_id, :basket_id)";

        Map<String, Long> param = Map.of(
            "product_id", productId,
            "basket_id", basketId
        );

        jdbcTemplate.update(sql, param);
    }

    public void deleteProductFromBasket(long productId, long basketId) {
        String sql = "DELETE FROM products_baskets "
            + "WHERE product_id=:product_id AND basket_id=:basket_id";

        Map<String, Long> param = Map.of(
            "product_id", productId,
            "basket_id", basketId
        );

        jdbcTemplate.update(sql, param);
    }

    public Optional<Product> getProductFromBasket(long productId, long basketId) {
        Map<String, Long> param = Map.of(
            "product_id", productId,
            "basket_id", basketId
        );

        Product product = jdbcTemplate.query(
                "SELECT * FROM products_baskets "
                    + "WHERE product_id=:product_id AND basket_id=:basket_id",
                param,
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(product);
    }

    public void deleteProductsFromBasket(long basketId) {
        String sql = "DELETE FROM products_baskets "
            + "WHERE basket_id=:basket_id";

        Map<String, Long> param = Map.of(
            "basket_id", basketId
        );

        jdbcTemplate.update(sql, param);
    }
}
