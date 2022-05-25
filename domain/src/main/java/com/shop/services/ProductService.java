package com.shop.services;

import com.shop.models.Product;
import com.shop.repositories.ProductRepository;
import com.shop.validators.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public ProductService(
        ProductRepository productRepository,
        ProductValidator productValidator
    ) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        productValidator.validate(id);
        Optional<Product> product = productRepository.findById(id);
        return product.orElseGet(Product::new);
    }

    public Product findByBarcode(String barcode) {
        productValidator.validate(barcode);
        Optional<Product> product = productRepository.findByBarcode(barcode);
        return product.orElseGet(Product::new);
    }

    public Product save(Product product) {
        productValidator.validate(product);
        productRepository.save(product);
        return product;
    }

    public void delete(long id) {
        productValidator.validate(id);
        productRepository.delete(id);
    }

    public void delete(String barcode) {
        productValidator.validate(barcode);
        productRepository.delete(barcode);
    }

    public byte[] findByIdImage(long id) {
        productValidator.validate(id);
        return productRepository.findByIdImage(id);
    }

    public void saveImage(byte[] image, long id) {
        productValidator.validate(id);
        productRepository.saveImage(image, id);
    }

    public List<Product> findRandomProducts(int count) {
        List<Product> products = new ArrayList<>();
        int size = findAll().size();

        if (count > size) {
            count = size;
        }

        int i = 0;
        while (i < count) {
            Optional<Product> product = productRepository.findById(getRandomIndex());
            if (product.isPresent()) {
                if (!products.contains(product.get())) {
                    products.add(product.get());
                    i++;
                }
            }
        }

        return products;
    }

    private long getRandomIndex() {
        Random random = new Random();
        List<Product> products = findAll();
        long maxRandomId = products.get(products.size() - 1).getId();

        List<Long> ids = new ArrayList<>();
        for (Product product : products) {
            ids.add(product.getId());
        }

        long id = 0;
        while (!ids.contains(id)) {
            id = random.nextInt((int) maxRandomId) + 1;
        }

        return id;
    }
}
