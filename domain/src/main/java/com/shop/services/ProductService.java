package com.shop.services;

import com.shop.exceptions.NotFoundException;
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
    private final ProductValidator validator;

    public ProductService(
        ProductRepository productRepository,
        ProductValidator validator
    ) {
        this.productRepository = productRepository;
        this.validator = validator;
    }

    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product addProduct(Product product) {
        validator.validate(product);
        productRepository.addProduct(product);
        return product;
    }

    public void deleteProduct(long id) {
        validator.validate(id);
        productRepository.deleteProduct(id);
    }

    public Product getProduct(long id) {
        validator.validate(id);
        Optional<Product> product = productRepository.getProduct(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        } else {
            return product.get();
        }
    }

    public List<Product> getRandomProducts(int count) {
        List<Product> products = new ArrayList<>();
        int size = getProducts().size();

        if (count > size) {
            count = size;
        }

        int i = 0;
        while (i < count) {
            Optional<Product> product = productRepository.getProduct(getRandomIndex());
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
        List<Product> products = getProducts();
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
