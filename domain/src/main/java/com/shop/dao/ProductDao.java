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
public class ProductDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Product> getProducts() {
        return jdbcTemplate.query(
            "SELECT * FROM product",
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    public void addProduct(Product product) {
        jdbcTemplate.update(
            "INSERT INTO product (name, describe, price, barcode, in_stock/*, image*/) "
                + "VALUES (:name, :describe, :price, :barcode, :in_stock/*, :image*/)",
            Map.of(
                "name", product.getName(),
                "describe", product.getDescribe(),
                "price", product.getPrice(),
                "barcode", product.getBarcode(),
                "in_stock", product.isInStock()/*,
                "image", product.getImage()*/
            )
        );
    }

    public void deleteProduct(long id) {
        jdbcTemplate.update(
            "DELETE FROM product WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Product> getProduct(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM product WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny();
    }

    public Optional<Product> getProduct(String barcode) {
        return jdbcTemplate.query(
                "SELECT * FROM product WHERE barcode=:barcode",
                Map.of("barcode", barcode),
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny();
    }

    public void deleteProduct(String barcode) {
        jdbcTemplate.update(
            "DELETE FROM product WHERE barcode=:barcode",
            Map.of("barcode", barcode)
        );
    }

    public byte[] getImage(long id) {
        return jdbcTemplate.queryForObject(
            "SELECT image FROM product WHERE id=:id",
            Map.of("id", id),
            (rs, rowNum) -> rs.getBytes(1)
        );
    }

    public void addImage(byte[] image, long id) {
        jdbcTemplate.update(
            "UPDATE product SET image=:image WHERE id=:id",
            Map.of(
                "image", image,
                "id", id
            )
        );
    }
}
