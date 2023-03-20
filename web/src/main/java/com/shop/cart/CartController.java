package com.shop.cart;

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
    public Cart saveCart(@RequestBody Cart cart) {
        return cartService.save(cart);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable long id) {
        cartService.delete(Cart.of(0, 0).withId(id));
    }
}
