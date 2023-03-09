package com.shop.services;

import com.shop.models.Cart;
import com.shop.repositories.CartRepository;
import com.shop.validators.CartValidator;
import com.shop.validators.PersonValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartValidator cartValidator;
    private final PersonService personService;
    private final PersonValidator personValidator;

    public CartService(
        CartRepository cartRepository,
        CartValidator cartValidator,
        PersonService personService,
        PersonValidator personValidator
    ) {
        this.cartRepository = cartRepository;
        this.cartValidator = cartValidator;
        this.personService = personService;
        this.personValidator = personValidator;
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart findById(long id) {
        cartValidator.validate(id);
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.orElseGet(Cart::new);
    }

    public Cart findByPerson(long personId) {
        personValidator.validate(personId, personService.findAll());
        Optional<Cart> cart = cartRepository.findByPerson(personId);
        return cart.orElseGet(Cart::new);
    }

    public Cart save(double totalCost, long personId) {
        cartValidator.validate(totalCost);
        personValidator.validate(personId, personService.findAll());
        return cartRepository.save(totalCost, personId);
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
