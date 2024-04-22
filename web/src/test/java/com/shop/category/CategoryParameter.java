package com.shop.category;

public class CategoryParameter {
    public static Category getCategoryWithId() {
        return getCategoryWithoutId().withId(getCategoryId());
    }

    public static Category getCategoryWithId2() {
        return getCategoryWithoutId2().withId(getCategoryId());
    }

    public static Category getCategoryWithoutId() {
        return Category.of(getName());
    }

    public static Category getCategoryWithoutId2() {
        return Category.of("name2");
    }

    public static long getCategoryId() {
        return 1L;
    }

    public static String getName() {
        return "name";
    }
}
