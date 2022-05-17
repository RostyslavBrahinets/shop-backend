package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Category;
import com.shop.repositories.ProductCategoryRepository;
import com.shop.validators.CategoryValidator;
import com.shop.validators.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductValidator productValidator;
    private final CategoryValidator categoryValidator;

    public ProductCategoryService(
        ProductCategoryRepository productCategoryRepository,
        ProductValidator productValidator,
        CategoryValidator categoryValidator
    ) {
        this.productCategoryRepository = productCategoryRepository;
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

    public void addProductToCategory(long productId, long categoryId) {
        productValidator.validate(productId);
        categoryValidator.validate(categoryId);
        productCategoryRepository.addProductToCategory(productId, categoryId);
    }
}
