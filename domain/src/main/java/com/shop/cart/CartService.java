package com.shop.cart;

import com.shop.user.UserService;
import com.shop.user.UserValidator;
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

    public Cart save(Cart cart) {
        cartValidator.validate(cart.getTotalCost());
        userValidator.validate(cart.getUserId(), userService.findAll());
        cartRepository.save(cart.getTotalCost(), cart.getUserId());
        return Cart.of(cart.getTotalCost(), 0).withId(cartRepository.findAll().size() + 1);
    }

    public Cart update(Cart cart) {
        cartValidator.validate(cart.getId());
        cartValidator.validate(cart.getTotalCost());
        cartRepository.update(cart.getId(), cart.getTotalCost());
        return Cart.of(cart.getTotalCost(), 0).withId(cart.getId());
    }

    public void delete(Cart cart) {
        cartValidator.validate(cart.getId());
        cartRepository.delete(cart.getId());
    }
}
