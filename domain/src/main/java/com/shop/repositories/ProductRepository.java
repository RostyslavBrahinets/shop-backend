package com.shop.repositories;

import com.shop.dao.ProductDao;
import com.shop.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    public void addProduct(Product person) {
        productDao.addProduct(person);
    }

    public void updateProduct(long id, Product product) {
        productDao.updateProduct(id, product);
    }

    public void deleteProduct(long id) {
        productDao.deleteProduct(id);
    }

    public Optional<Product> getProduct(long id) {
        return productDao.getProduct(id);
    }
}
