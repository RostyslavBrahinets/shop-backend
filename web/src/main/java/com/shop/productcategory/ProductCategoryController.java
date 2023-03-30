package com.shop.productcategory;

import com.shop.product.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductCategoryController.PRODUCT_CATEGORY_URL)
public class ProductCategoryController {
    public static final String PRODUCT_CATEGORY_URL = "/api/product-category";
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    public List<Product> findAllProductsInCategory(@PathVariable long id) {
        return productCategoryService.findAllProductsInCategory(id);
    }

    @PostMapping
    public ProductCategoryDto saveProductsToCategory(
        @RequestBody ProductCategoryDto productCategoryDto
    ) {
        productCategoryService.saveProductToCategory(
            productCategoryDto.barcode(),
            productCategoryDto.category()
        );
        return productCategoryDto;
    }

    @PutMapping
    public ProductCategoryDto updateCategoryForProduct(
        @RequestBody ProductCategoryDto productCategoryDto
    ) {
        productCategoryService.updateCategoryForProduct(
            productCategoryDto.barcode(),
            productCategoryDto.category()
        );
        return productCategoryDto;
    }

    @DeleteMapping("/{barcode}")
    public String deleteProductFromCategory(@PathVariable String barcode) {
        productCategoryService.deleteProductFromCategory(barcode);
        return "Product from Category Successfully Deleted";
    }
}
