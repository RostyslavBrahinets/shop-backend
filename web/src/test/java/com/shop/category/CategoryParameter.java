package com.shop.category;

public class CategoryParameter {
    public static Category getCategoryWithId() {
        return Category.of(getName()).withId(getCategoryId());
    }

    public static long getCategoryId() {
        return 1L;
    }

    public static String getName() {
        return "name";
    }
}
