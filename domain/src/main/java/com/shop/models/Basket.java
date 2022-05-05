package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class Basket implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private long id;
    private List<Product> products;
    private double totalCost;

    public Basket() {
    }

    public Basket(
        long id,
        List<Product> products,
        double totalCost
    ) {
        this.id = id;
        this.products = products;
        this.totalCost = totalCost;
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

        Basket basket = (Basket) o;
        return id == basket.id
            && totalCost == basket.totalCost
            && Objects.equals(products, basket.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products, totalCost);
    }

    @Override
    public String toString() {
        return "Basket{"
            + "id=" + id
            + ", products=" + products
            + ", totalCost=" + totalCost
            + '}';
    }
}
