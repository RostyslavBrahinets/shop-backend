package com.shop.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Basket implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private long id;
    private List<Product> products;
    private int totalCost;

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

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
