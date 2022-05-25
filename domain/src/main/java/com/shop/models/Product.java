package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Component
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;
    private long id;
    private String name;
    private String describe;
    private double price;
    private String barcode;
    private Category category;
    private boolean inStock;
    private byte[] image;

    public Product() {
    }

    public Product(
        long id,
        String name,
        String describe,
        double price,
        String barcode,
        boolean inStock
    ) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.price = price;
        this.barcode = barcode;
        this.inStock = inStock;
    }

    public static Product of(
        String name,
        String describe,
        double price,
        String barcode,
        boolean inStock
    ) {
        return new Product(
            0,
            name,
            describe,
            price,
            barcode,
            inStock
        );
    }

    public Product withId(long id) {
        return new Product(
            id,
            this.name,
            this.describe,
            this.price,
            this.barcode,
            this.inStock
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;
        return id == product.id
            && price == product.price
            && inStock == product.inStock
            && Objects.equals(name, product.name)
            && Objects.equals(describe, product.describe)
            && Objects.equals(barcode, product.barcode)
            && category == product.category
            && Arrays.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, describe, barcode, price, category, inStock);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "Product{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", describe='" + describe + '\''
            + ", barcode='" + barcode + '\''
            + ", price=" + price
            + ", category=" + category
            + ", inStock=" + inStock
            + '}';
    }
}
