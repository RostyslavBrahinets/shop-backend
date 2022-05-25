package com.shop.services;

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

    public List<Product> findAllProductsInCategory(long categoryId) {
        categoryValidator.validate(categoryId);
        return productCategoryRepository.findAllProductsInCategory(categoryId);
    }

    public Category findCategoryForProduct(long productId) {
        productValidator.validate(productId);
        Optional<Category> category = productCategoryRepository.findCategoryForProduct(productId);
        return category.orElseGet(Category::new);
    }

    public void saveProductToCategory(String barcode, String category) {
        productValidator.validate(barcode);
        categoryValidator.validate(category);
        Optional<Product> productOptional = productRepository.findByBarcode(barcode);
        Optional<Category> categoryOptional = categoryRepository.findByName(category);

        if (productOptional.isPresent() && categoryOptional.isPresent()) {
            productCategoryRepository.saveProductToCategory(
                productOptional.get().getId(),
                categoryOptional.get().getId()
            );
        }
    }
}
