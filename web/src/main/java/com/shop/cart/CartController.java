package com.shop.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(CartController.CARTS_URL)
public class CartController {
    public static final String CARTS_URL = "/api/v1/carts";
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> findAll() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public Cart findById(@PathVariable long id) {
        return cartService.findById(id);
    }

    @PostMapping
    public Cart save(@RequestBody Cart cart) {
        return cartService.save(cart);
    }

    @PutMapping("/{id}")
    public Cart update(
        @PathVariable long id,
        @RequestBody Cart cart
    ) {
        return cartService.update(id, cart);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        cartService.delete(Cart.of(0, 0).withId(id));
    }
}
