package com.shop.services;

import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.repositories.ProductCategoryRepository;
import com.shop.validators.CategoryValidator;
import com.shop.validators.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductValidator productValidator;
    private final CategoryValidator categoryValidator;

    public ProductCategoryService(
        ProductCategoryRepository productCategoryRepository,
        ProductService productService,
        CategoryService categoryService,
        ProductValidator productValidator,
        CategoryValidator categoryValidator
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productService = productService;
        this.categoryService = categoryService;
        this.productValidator = productValidator;
        this.categoryValidator = categoryValidator;
    }

    public List<Product> findAllProductsInCategory(long categoryId) {
        categoryValidator.validate(categoryId);
        return productCategoryRepository.findAllProductsInCategory(categoryId);
    }

    public void saveProductToCategory(String barcode, String nameOfCategory) {
        productValidator.validate(barcode);
        categoryValidator.validate(nameOfCategory);

        Product product = productService.findByBarcode(barcode);
        Category category = categoryService.findByName(nameOfCategory);

        productCategoryRepository.saveProductToCategory(
            product.getId(),
            category.getId()
        );
    }
}
