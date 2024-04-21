package com.shop.cart;

import java.util.List;

import static com.shop.user.UserParameter.getUserId;

public class CartParameter {
    public static Cart getCartWithId() {
        return getCartWithoutId().withId(getCartId());
    }

    static Cart getCartWithId2(double priceAmount) {
        return getCartWithoutId(priceAmount).withId(getCartId());
    }

    static Cart getCartWithId2(long userId) {
        return getCartWithoutId(userId).withId(getCartId2());
    }

    static Cart getCartWithId(double priceAmount) {
        return getCartWithoutId(priceAmount).withId(getCartId());
    }

    static Cart getCartWithoutId() {
        return Cart.of(getPriceAmount(), getUserId());
    }

    static Cart getCartWithoutId(long userId) {
        return Cart.of(getPriceAmount(), userId);
    }

    static Cart getCartWithoutId(double priceAmount) {
        return Cart.of(priceAmount, getUserId());
    }

    static long getCartId() {
        return 1L;
    }

    static long getCartId2() {
        return 2L;
    }

    static double getPriceAmount() {
        return 0;
    }

    static double getPriceAmount2() {
        return 100;
    }

    static List<Cart> getCarts() {
        return List.of();
    }
}
