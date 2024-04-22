package com.shop.cart;

import static com.shop.user.UserParameter.getUserId;

public class CartParameter {
    static Cart getCartWithId() {
        return getCartWithoutId().withId(getCartId());
    }

    static Cart getCartWithId2() {
        return getCartWithoutId2().withId(getCartId());
    }

    static Cart getCartWithoutId() {
        return Cart.of(0, getUserId());
    }

    static Cart getCartWithoutId2() {
        return Cart.of(100, getUserId());
    }

    static long getCartId() {
        return 1L;
    }
}
