package com.shop.dto;

import org.springframework.stereotype.Component;

@Component
public class ProductCategoryDto {
    private String barcode;
    private String category;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
