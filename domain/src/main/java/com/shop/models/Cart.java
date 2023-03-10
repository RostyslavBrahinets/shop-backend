package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private long id;
    private List<Product> products;
    private double totalCost;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public Cart(
        long id,
        double totalCost
    ) {
        this.id = id;
        this.products = new ArrayList<>();
        this.totalCost = totalCost;
    }

    public static Cart of(double totalCost) {
        return new Cart(0, totalCost);
    }

    public Cart withId(long id) {
        return new Cart(id, this.totalCost);
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

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cart cart = (Cart) o;
        return id == cart.id
            && totalCost == cart.totalCost
            && Objects.equals(products, cart.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products, totalCost);
    }

    @Override
    public String toString() {
        return "Cart{"
            + "id=" + id
            + ", products=" + products
            + ", totalCost=" + totalCost
            + '}';
    }
}