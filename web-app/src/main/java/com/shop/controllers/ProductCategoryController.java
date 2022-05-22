package com.shop.controllers;

import com.shop.models.Product;
import com.shop.services.ProductCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ProductCategoryController.PRODUCT_CATEGORY_URL)
public class ProductCategoryController {
    public static final String PRODUCT_CATEGORY_URL = "/web-api/product-category";
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    public List<Product> findAllProductsInCategory(@PathVariable long id) {
        return productCategoryService.getProductsInCategory(id);
    }
}
