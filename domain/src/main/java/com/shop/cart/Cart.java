package com.shop.cart;

import com.shop.product.Product;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private long id;
    private List<Product> products;
    private double priceAmount;
    private long userId;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public Cart(
        long id,
        double priceAmount,
        long userId
    ) {
        this.id = id;
        this.priceAmount = priceAmount;
        this.products = new ArrayList<>();
        this.userId = userId;
    }

    public static Cart of(double priceAmount, long userId) {
        return new Cart(0, priceAmount, userId);
    }

    public Cart withId(long id) {
        return new Cart(id, this.priceAmount, this.userId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(double priceAmount) {
        this.priceAmount = priceAmount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart that = (Cart) o;
        return Objects.equals(id, that.id)
            && Objects.equals(priceAmount, that.priceAmount)
            && Objects.equals(products, that.products)
            && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products, priceAmount, userId);
    }

    @Override
    public String toString() {
        return "Cart{"
            + "id=" + id
            + ", products=" + products
            + ", priceAmount=" + priceAmount
            + '}';
    }
}
