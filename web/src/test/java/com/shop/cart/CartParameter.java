package com.shop.cart;

import static com.shop.user.UserParameter.getUserId;

public class CartParameter {
    static Cart getCartWithId() {
        return Cart.of(0, getUserId()).withId(getCartId());
    }

    static long getCartId() {
        return 1L;
    }
}
