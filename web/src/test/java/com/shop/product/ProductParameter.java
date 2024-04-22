package com.shop.product;

public class ProductParameter {
    public static Product getProductWithId() {
        return getProductWithoutId().withId(getProductId());
    }

    public static Product getProductWithId2() {
        return getProductWithoutId2().withId(getProductId());
    }

    public static Product getProductWithoutId() {
        return Product.of(
            "name",
            "describe",
            0,
            getBarcode(),
            true,
            new byte[]{1, 1, 1}
        );
    }

    public static Product getProductWithoutId2() {
        return Product.of(
            "name2",
            "describe2",
            100,
            "456",
            false,
            new byte[]{1, 1, 1}
        );
    }

    public static long getProductId() {
        return 1L;
    }

    public static String getBarcode() {
        return "123";
    }
}
