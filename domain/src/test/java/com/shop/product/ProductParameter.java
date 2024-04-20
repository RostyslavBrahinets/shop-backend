package com.shop.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProductParameter {
    public static Product getProductWithId() {
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

    public static long getProductId() {
        return 1L;
    }

    public static long getProductId2() {
        return 2L;
    }

    public static String getName() {
        return "name";
    }

    static String getName2() {
        return "name2";
    }

    public static String getDescribe() {
        return "describe";
    }

    static String getDescribe2() {
        return "describe2";
    }

    public static double getPrice() {
        return 0;
    }

    static double getPrice2() {
        return 100;
    }

    public static String getBarcode() {
        return "123";
    }

    static String getBarcode2() {
        return "456";
    }

    public static boolean isInStock() {
        return true;
    }

    static boolean isInStock2() {
        return false;
    }

    public static byte[] getImage() {
        return new byte[]{1, 1, 1};
    }

    static byte[] getImage2() {
        return new byte[]{127, 127, 127};
    }

    static List<Product> getProducts() {
        return List.of();
    }

    public static Map<String, Serializable> getMapOfEntries(String barcode) {
        return Map.ofEntries(
            Map.entry("name", getName()),
            Map.entry("describe", getDescribe()),
            Map.entry("price", getPrice()),
            Map.entry("barcode", barcode),
            Map.entry("in_stock", isInStock()),
            Map.entry("image", getImage())
        );
    }
}
