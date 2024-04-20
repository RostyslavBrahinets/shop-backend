package com.shop.cart;

import com.shop.interfaces.ServiceInterface;
import com.shop.user.User;
import com.shop.user.UserRepository;
import com.shop.user.UserValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ServiceInterface<Cart> {
    private final CartRepository cartRepository;
    private final CartValidator cartValidator;
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public CartService(
        CartRepository cartRepository,
        CartValidator cartValidator,
        UserRepository userRepository,
        UserValidator userValidator
    ) {
        this.cartRepository = cartRepository;
        this.cartValidator = cartValidator;
        this.userRepository = userRepository;
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
        userValidator.validate(cart.getUserId(), userRepository.findAll());
        cartRepository.save(cart);
        cart.setId(cartRepository.findAll().size() + 1L);
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
        userValidator.validate(user.getId(), userRepository.findAll());
        Optional<Cart> cart = cartRepository.findByUser(user);
        return cart.orElseGet(Cart::new);
    }
}
