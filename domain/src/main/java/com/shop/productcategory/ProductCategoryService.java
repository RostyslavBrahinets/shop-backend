package com.shop.productcategory;

import com.shop.category.Category;
import com.shop.category.CategoryService;
import com.shop.category.CategoryValidator;
import com.shop.product.Product;
import com.shop.product.ProductService;
import com.shop.product.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        categoryValidator.validate(categoryId, categoryService.findAll());
        return productCategoryRepository.findAllProductsInCategory(categoryId);
    }

    public void saveProductToCategory(String barcode, String nameOfCategory) {
        productValidator.validateBarcode(barcode, productService.findAll());
        categoryValidator.validate(nameOfCategory, categoryService.findAll());

        Product product = productService.findByBarcode(barcode);
        Category category = categoryService.findByName(nameOfCategory);

        productCategoryRepository.saveProductToCategory(
            product.getId(),
            category.getId()
        );
    }

    public void updateCategoryForProduct(String barcode, String nameOfCategory) {
        productValidator.validateBarcode(barcode, productService.findAll());
        categoryValidator.validate(nameOfCategory, categoryService.findAll());

        Product product = productService.findByBarcode(barcode);
        Category category = categoryService.findByName(nameOfCategory);

        productCategoryRepository.updateCategoryForProduct(
            product.getId(),
            category.getId()
        );
    }

    public void deleteProductFromCategory(String barcode) {
        productValidator.validateBarcode(barcode, productService.findAll());

        Product product = productService.findByBarcode(barcode);
        Optional<Category> categoryOptional = productCategoryRepository.findCategoryForProduct(product.getId());

        categoryOptional.ifPresent(
            category -> productCategoryRepository.deleteProductFromCategory(
                product.getId(),
                category.getId()
            )
        );
    }
}
