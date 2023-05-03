package com.shop.cart;

import com.shop.interfaces.ServiceInterface;
import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ServiceInterface<Cart> {
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

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(long id) {
        cartValidator.validate(id);
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.orElseGet(Cart::new);
    }

    @Override
    public Cart save(Cart cart) {
        cartValidator.validate(cart.getPriceAmount());
        userValidator.validate(cart.getUserId(), userService.findAll());
        cartRepository.save(cart);

        if (cartRepository.findAll().size() > 0) {
            cart.setId(cartRepository.findAll().get(cartRepository.findAll().size() - 1).getId());
        } else {
            cart.setId(1);
        }

        return cart;
    }

    @Override
    public Cart update(long id, Cart cart) {
        cartValidator.validate(id);
        cartValidator.validate(cart.getPriceAmount());
        cartRepository.update(id, cart);
        return cart;
    }

    @Override
    public void delete(Cart cart) {
        cartValidator.validate(cart.getId());
        cartRepository.delete(cart);
    }

    public Cart findByUser(User user) {
        userValidator.validate(user.getId(), userService.findAll());
        Optional<Cart> cart = cartRepository.findByUser(user);
        return cart.orElseGet(Cart::new);
    }
}
