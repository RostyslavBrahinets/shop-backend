package com.shop.productcategory;

import static com.shop.category.CategoryParameter.getName;
import static com.shop.product.ProductParameter.getBarcode;

public class ProductCategoryParameter {
    public static ProductCategoryDto getProductCategory() {
        return new ProductCategoryDto(getBarcode(), getName());
    }
}
