package com.shop.productcategory;

import com.shop.category.Category;
import com.shop.category.CategoryService;
import com.shop.category.CategoryValidator;
import com.shop.product.Product;
import com.shop.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategoryValidator categoryValidator;

    public ProductCategoryService(
        ProductCategoryRepository productCategoryRepository,
        ProductService productService,
        CategoryService categoryService,
        CategoryValidator categoryValidator
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productService = productService;
        this.categoryService = categoryService;
        this.categoryValidator = categoryValidator;
    }

    public List<Product> findAllProductsInCategory(long categoryId) {
        categoryValidator.validate(categoryId, categoryService.findAll());
        return productCategoryRepository.findAllProductsInCategory(categoryId);
    }

    public void saveProductToCategory(String barcode, String nameOfCategory) {
        productCategoryRepository.saveProductToCategory(
            productService.findByBarcode(barcode).getId(),
            categoryService.findByName(nameOfCategory).getId()
        );
    }

    public void updateCategoryForProduct(String barcode, String nameOfCategory) {
        productCategoryRepository.updateCategoryForProduct(
            productService.findByBarcode(barcode).getId(),
            categoryService.findByName(nameOfCategory).getId()
        );
    }

    public void deleteProductFromCategory(String barcode) {
        Optional<Category> categoryOptional = productCategoryRepository
            .findCategoryForProduct(
                productService.findByBarcode(barcode).getId()
            );

        categoryOptional.ifPresent(
            category -> productCategoryRepository.deleteProductFromCategory(
                productService.findByBarcode(barcode).getId(),
                category.getId()
            )
        );
    }
}
