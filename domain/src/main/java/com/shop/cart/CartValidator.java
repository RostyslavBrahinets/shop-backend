package com.shop.cart;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.cart.Cart;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartValidator {
    public void validate(double priceAmount) {
        if (priceAmount < 0) {
            throw new ValidationException("Price amount of products in cart is invalid");
        }
    }

    public void validate(long id, List<Cart> carts) {
        List<Long> ids = new ArrayList<>();

        for (Cart cart : carts) {
            ids.add(cart.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Cart not found");
        }
    }
}