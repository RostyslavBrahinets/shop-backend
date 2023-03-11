package com.shop.services;

import com.shop.models.Cart;
import com.shop.repositories.CartRepository;
import com.shop.validators.CartValidator;
import com.shop.validators.UserValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartValidator cartValidator;
    private final UserService userService;
    private final UserValidator userValidator;

    public CartService(
        CartRepository cartRepository,
        CartValidator cartValidator,
        UserService userService,
        UserValidator userValidator
    ) {
        this.cartRepository = cartRepository;
        this.cartValidator = cartValidator;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart findById(long id) {
        cartValidator.validate(id);
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.orElseGet(Cart::new);
    }

    public Cart findByUser(long userId) {
        userValidator.validate(userId, userService.findAll());
        Optional<Cart> cart = cartRepository.findByUser(userId);
        return cart.orElseGet(Cart::new);
    }

    public Cart save(double totalCost, long userId) {
        cartValidator.validate(totalCost);
        userValidator.validate(userId, userService.findAll());
        return cartRepository.save(totalCost, userId);
    }

    public Cart update(long id, double totalCost) {
        cartValidator.validate(id);
        cartValidator.validate(totalCost);
        return cartRepository.update(id, totalCost);
    }

    public void delete(long id) {
        cartValidator.validate(id);
        cartRepository.delete(id);
    }
}
