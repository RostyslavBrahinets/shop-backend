package com.shop.dao;

import com.shop.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM product",
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public Optional<Product> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM product WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny();
    }

    public Optional<Product> findByBarcode(String barcode) {
        return jdbcTemplate.query(
                "SELECT * FROM product WHERE barcode=:barcode",
                Map.ofEntries(Map.entry("barcode", barcode)),
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny();
    }

    public void save(
        String name,
        String describe,
        double price,
        String barcode,
        boolean inStock,
        byte[] image
    ) {
        jdbcTemplate.update(
            "INSERT INTO product (name, describe, price, barcode, in_stock, image) "
                + "VALUES (:name, :describe, :price, :barcode, :in_stock, :image)",
            Map.ofEntries(
                Map.entry("name", name),
                Map.entry("describe", describe),
                Map.entry("price", price),
                Map.entry("barcode", barcode),
                Map.entry("in_stock", inStock),
                Map.entry("image", image)
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM product WHERE id=:id",
            Map.ofEntries(Map.entry("id", id))
        );
    }

    public void delete(String barcode) {
        jdbcTemplate.update(
            "DELETE FROM product WHERE barcode=:barcode",
            Map.ofEntries(Map.entry("barcode", barcode))
        );
    }

    public void saveImageForProduct(byte[] image, long id) {
        jdbcTemplate.update(
            "UPDATE product SET image=:image WHERE id=:id",
            Map.ofEntries(
                Map.entry("image", image),
                Map.entry("id", id)
            )
        );
    }
}
