package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductValidator {
    public void validate(
        String name,
        String describe,
        double price,
        String barcode,
        List<Product> products
    ) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is invalid");
        } else if (describe == null || describe.isBlank()) {
            throw new ValidationException("Describe is invalid");
        } else if (price < 0) {
            throw new ValidationException("Price is invalid");
        } else if (invalidBarcode(barcode, products) == 1) {
            throw new ValidationException("Barcode is invalid");
        } else if (invalidBarcode(barcode, products) == 2) {
            throw new ValidationException("Barcode already in use");
        }
    }

    public void validate(long id, List<Product> products) {
        List<Long> ids = new ArrayList<>();

        for (Product product : products) {
            ids.add(product.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Product not found");
        }
    }

    public void validate(String barcode, List<Product> products) {
        if (barcode == null || barcode.isBlank()) {
            throw new ValidationException("Barcode is invalid");
        }

        List<String> barcodes = new ArrayList<>();

        for (Product product : products) {
            barcodes.add(product.getBarcode());
        }

        if (!barcodes.contains(barcode)) {
            throw new NotFoundException("Barcode not found");
        }
    }

    private byte invalidBarcode(String barcode, List<Product> products) {
        if (barcode == null || barcode.isBlank()) {
            return (byte) 1;
        }

        List<String> barcodes = new ArrayList<>();

        for (Product product : products) {
            barcodes.add(product.getBarcode());
        }

        if (barcodes.contains(barcode)) {
            return (byte) 2;
        }

        return (byte) 0;
    }
}