package com.shop.product;

import java.util.List;

public class ProductParameter {
    static Product getProductWithId() {
        return getProductWithId(getProductId(), getBarcode());
    }

    static Product getProductWithId(long id, String barcode) {
        return getProductWithoutId(barcode).withId(id);
    }

    static Product getProductWithId2() {
        return getProductWithoutId2().withId(getProductId());
    }

    static Product getProductWithoutId() {
        return getProductWithoutId(getBarcode());
    }

    static Product getProductWithoutId(String barcode) {
        return Product.of(
            getName(),
            getDescribe(),
            getPrice(),
            barcode,
            isInStock(),
            getImage()
        );
    }

    static Product getProductWithoutId2() {
        return Product.of(
            getName2(),
            getDescribe2(),
            getPrice2(),
            getBarcode2(),
            isInStock2(),
            getImage()
        );
    }

    static long getCategoryId() {
        return 1L;
    }

    static long getProductId() {
        return 1L;
    }

    static long getProductId2() {
        return 2L;
    }

    static String getName() {
        return "name";
    }

    static String getName2() {
        return "name2";
    }

    static String getDescribe() {
        return "describe";
    }

    static String getDescribe2() {
        return "describe2";
    }

    static double getPrice() {
        return 0;
    }

    static double getPrice2() {
        return 100;
    }

    static String getBarcode() {
        return "123";
    }

    static String getBarcode2() {
        return "456";
    }

    static boolean isInStock() {
        return true;
    }

    static boolean isInStock2() {
        return false;
    }

    static byte[] getImage() {
        return new byte[]{1, 1, 1};
    }

    static byte[] getImage2() {
        return new byte[]{127, 127, 127};
    }

    static List<Product> getProducts() {
        return List.of();
    }
}
