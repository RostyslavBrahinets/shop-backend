package com.shop.product;

import com.shop.interfaces.RepositoryInterface;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository implements RepositoryInterface<Product> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM product",
            new BeanPropertyRowMapper<>(Product.class)
        );
    }

    @Override
    public Optional<Product> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM product WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny();
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update(
            "INSERT INTO product (name, describe, price, barcode, in_stock, image) "
                + "VALUES (:name, :describe, :price, :barcode, :in_stock, :image)",
            Map.ofEntries(
                Map.entry("name", product.getName()),
                Map.entry("describe", product.getDescribe()),
                Map.entry("price", product.getPrice()),
                Map.entry("barcode", product.getBarcode()),
                Map.entry("in_stock", product.isInStock()),
                Map.entry("image", product.getImage())
            )
        );
    }

    @Override
    public void update(long id, Product product) {
        jdbcTemplate.update(
            "UPDATE product SET "
                + "name=:name, "
                + "describe=:describe, "
                + "price=:price, "
                + "barcode=:barcode, "
                + "in_stock=:in_stock, "
                + "image=:image "
                + "WHERE id=:id",
            Map.ofEntries(
                Map.entry("name", product.getName()),
                Map.entry("describe", product.getDescribe()),
                Map.entry("price", product.getPrice()),
                Map.entry("barcode", product.getBarcode()),
                Map.entry("in_stock", product.isInStock()),
                Map.entry("image", product.getImage()),
                Map.entry("id", id)
            )
        );
    }

    @Override
    public void delete(Product product) {
        jdbcTemplate.update(
            "DELETE FROM product WHERE barcode=:barcode",
            Map.ofEntries(Map.entry("barcode", product.getBarcode()))
        );
    }

    public Optional<Product> findByBarcode(String barcode) {
        return jdbcTemplate.query(
                "SELECT * FROM product WHERE barcode=:barcode",
                Map.ofEntries(Map.entry("barcode", barcode)),
                new BeanPropertyRowMapper<>(Product.class)
            )
            .stream().findAny();
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
