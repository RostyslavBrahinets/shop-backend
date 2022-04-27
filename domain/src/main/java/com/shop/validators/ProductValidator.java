package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.repositories.ProductRepository;

public class ProductValidator {
    private final ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Product product) {
        String name = product.getName();
        String describe = product.getDescribe();
        double price = product.getPrice();
        Category category = product.getCategory();

        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is invalid");
        } else if (describe == null || describe.isBlank()) {
            throw new ValidationException("Describe is invalid");
        } else if (price < 0) {
            throw new ValidationException("Price is invalid");
        } else if (category == null || category.toString().isBlank()) {
            throw new ValidationException("Category is invalid");
        }
    }

    public void validate(int id) {
        if (id < 1 || id > productRepository.getProducts().size()) {
            throw new NotFoundException("Product not found");
        }
    }
}