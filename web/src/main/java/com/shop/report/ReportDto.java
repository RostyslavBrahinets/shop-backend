package com.shop.report;

import com.shop.product.Product;

import java.util.List;

public record ReportDto(
    List<Product> products,
    double priceAmount
) {
}
