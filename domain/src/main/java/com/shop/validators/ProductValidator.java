package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Product;
import com.shop.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductValidator {
    private final ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(
        String name,
        String describe,
        double price,
        String barcode
    ) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is invalid");
        } else if (describe == null || describe.isBlank()) {
            throw new ValidationException("Describe is invalid");
        } else if (price < 0) {
            throw new ValidationException("Price is invalid");
        } else if (barcode == null || barcode.isBlank()) {
            throw new ValidationException("Barcode is invalid");
        }
    }

    public void validate(long id) {
        List<Long> ids = new ArrayList<>();

        for (Product product : productRepository.findAll()) {
            ids.add(product.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Product not found");
        }
    }

    public void validate(String barcode) {
        if (barcode == null || barcode.isBlank()) {
            throw new ValidationException("Barcode is invalid");
        }
    }
}