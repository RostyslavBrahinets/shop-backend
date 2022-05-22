package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.repositories.CategoryRepository;
import com.shop.repositories.ProductCategoryRepository;
import com.shop.repositories.ProductRepository;
import com.shop.validators.CategoryValidator;
import com.shop.validators.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductValidator productValidator;
    private final CategoryValidator categoryValidator;

    public ProductCategoryService(
        ProductCategoryRepository productCategoryRepository,
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        ProductValidator productValidator,
        CategoryValidator categoryValidator
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productValidator = productValidator;
        this.categoryValidator = categoryValidator;
    }

    public Category getCategoryForProduct(long productId) {
        Optional<Category> category = productCategoryRepository.getCategoryForProduct(productId);

        if (category.isEmpty()) {
            throw new NotFoundException("Category Not Found");
        } else {
            return category.get();
        }
    }

    public void addProductToCategory(String barcode, String category) {
        productValidator.validate(barcode);
        categoryValidator.validate(category);
        Optional<Product> productOptional = productRepository.getProduct(barcode);
        Optional<Category> categoryOptional = categoryRepository.getCategory(category);

        if (productOptional.isPresent() && categoryOptional.isPresent()) {
            productCategoryRepository.addProductToCategory(
                productOptional.get().getId(),
                categoryOptional.get().getId()
            );
        }
    }

    public List<Product> getProductsInCategory(long categoryId) {
        categoryValidator.validate(categoryId);
        return productCategoryRepository.getProductsInCategory(categoryId);
    }
}
