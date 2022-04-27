package com.shop.repositories;

import com.shop.dao.ProductDao;
import com.shop.models.Product;

import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    public void addPerson(Product person) {
        productDao.addProduct(person);
    }

    public void updateProduct(int id, Product product) {
        productDao.updateProduct(id, product);
    }

    public void deleteProduct(int id) {
        productDao.deleteProduct(id);
    }

    public Optional<Product> getProduct(int id) {
        return productDao.getProduct(id);
    }
}
