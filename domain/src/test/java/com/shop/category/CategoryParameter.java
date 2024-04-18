package com.shop.category;

import java.util.List;

public class CategoryParameter {
    public static Category getCategoryWithId() {
        return getCategoryWithoutId().withId(getCategoryId());
    }

    static Category getCategoryWithId(long id, String name) {
        return getCategoryWithoutId(name).withId(id);
    }

    static Category getCategoryWithoutId() {
        return Category.of(getName());
    }

    static Category getCategoryWithoutId(String name) {
        return Category.of(name);
    }

    static long getCategoryId() {
        return 1L;
    }

    static long getCategoryId2() {
        return 2L;
    }

    public static String getName() {
        return "name";
    }

    static String getName2() {
        return "name2";
    }

    public static List<Category> getCategories() {
        return List.of();
    }
}
