package com.shop.productcategory;

import com.shop.product.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductCategoryController.PRODUCT_CATEGORY_URL)
public class ProductCategoryController {
    public static final String PRODUCT_CATEGORY_URL = "/web-api/product-category";
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    public List<Product> findAllProductsInCategory(@PathVariable long id) {
        return productCategoryService.findAllProductsInCategory(id);
    }

    @PostMapping
    public void saveProductsToCategory(
        @RequestBody ProductCategoryDto productCategoryDto
    ) {
        productCategoryService.saveProductToCategory(
            productCategoryDto.getBarcode(),
            productCategoryDto.getCategory()
        );
    }
}
