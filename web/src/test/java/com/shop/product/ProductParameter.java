package com.shop.product;

public class ProductParameter {
    public static Product getProductWithId() {
        return Product.of(
            "name",
            "describe",
            0,
            getBarcode(),
            true,
            new byte[]{1, 1, 1}
        ).withId(getProductId());
    }

    public static long getProductId() {
        return 1L;
    }

    public static String getBarcode() {
        return "123";
    }
}
