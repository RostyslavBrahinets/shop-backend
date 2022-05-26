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

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Optional<Product> findById(long id) {
        return productDao.findById(id);
    }

    public Optional<Product> findByBarcode(String barcode) {
        return productDao.findByBarcode(barcode);
    }

    public Product save(Product product) {
        productDao.save(
            product.getName(),
            product.getDescribe(),
            product.getPrice(),
            product.getBarcode(),
            product.isInStock(),
            product.getImage()
        );

        return product;
    }

    public void delete(String barcode) {
        productDao.delete(barcode);
    }

    public void saveImageForProduct(byte[] image, long id) {
        productDao.saveImageForProduct(image, id);
    }

    public int count() {
        return productDao.findAll().size();
    }
}
