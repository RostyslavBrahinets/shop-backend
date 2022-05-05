package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductsBasketsDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Product> getProductsFromBasket(long idBasket) {
        Map<String, Long> param = Map.of(
            "id_basket", idBasket
        );

        List<Long> idProducts = jdbcTemplate.query(
            "SELECT * FROM products_baskets WHERE id_basket=:id_basket",
            param,
            new BeanPropertyRowMapper<>(Long.class)
        );

        List<Product> products = new ArrayList<>();

        for (Long id : idProducts) {
            Map<String, Long> paramId = Map.of(
                "id", id
            );

            Product product = jdbcTemplate.query(
                    "SELECT * FROM product WHERE id=:id",
                    paramId,
                    new BeanPropertyRowMapper<>(Product.class)
                )
                .stream().findAny().orElse(null);

            products.add(product);
        }

        return products;
    }

    public void addProductToBasket(long idProduct, long idBasket) {
        String sql = "INSERT INTO products_baskets (id_product, id_basket)"
            + " VALUES (:id_product, :id_basket)";

        Map<String, Long> param = Map.of(
            "id_product", idProduct,
            "id_basket", idBasket
        );

        jdbcTemplate.update(sql, param);
    }

    public void deleteProductFromBasket(long idProduct, long idBasket) {
        String sql = "DELETE FROM products_baskets "
            + "WHERE id_product=:id_product AND id_basket=:id_basket";

        Map<String, Long> param = Map.of(
            "id_product", idProduct,
            "id_basket", idBasket
        );

        jdbcTemplate.update(sql, param);
    }

    public Optional<Product> getProductFromBasket(long idProduct, long idBasket) {
        Map<String, Long> param = Map.of(
            "id_product", idProduct,
            "id_basket", idBasket
        );

        Product product = jdbcTemplate.query(
                "SELECT * FROM products_baskets "
                    + "WHERE id_product=:id_product AND id_basket=:id_basket",
                param,
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny().orElse(null);

        return Optional.ofNullable(product);
    }
}
