package com.shop.controllers;

import com.shop.models.Cart;
import com.shop.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CartController.CARTS_URL)
public class CartController {
    public static final String CARTS_URL = "/web-api/carts";
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> findAllCarts() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public Cart findByIdCart(@PathVariable long id) {
        return cartService.findById(id);
    }

    @PostMapping
    public Cart saveCart(
        @RequestBody Cart cart,
        @RequestBody long userId
    ) {
        return cartService.save(cart.getTotalCost(), userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable long id) {
        cartService.delete(id);
    }
}
