package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Product;
import com.shop.repositories.ProductRepository;
import com.shop.validators.ProductValidator;

import java.util.List;
import java.util.Optional;

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

    public Product updateProduct(long id, Product product) {
        validator.validate(id);
        validator.validate(product);
        productRepository.updateProduct(id, product);
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
}
