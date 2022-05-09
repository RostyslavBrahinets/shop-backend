package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
import com.shop.validators.BasketValidator;
import com.shop.validators.PersonValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketValidator basketValidator;
    private final PersonValidator personValidator;

    public BasketService(
        BasketRepository basketRepository,
        BasketValidator basketValidator,
        PersonValidator personValidator
    ) {
        this.basketRepository = basketRepository;
        this.basketValidator = basketValidator;
        this.personValidator = personValidator;
    }

    public List<Basket> getBaskets() {
        return basketRepository.getBaskets();
    }

    public Basket addBasket(Basket basket, long personId) {
        basketValidator.validate(basket);
        personValidator.validate(personId);
        basketRepository.addBasket(basket, personId);
        return basket;
    }

    public Basket updateBasket(long id, Basket basket) {
        basketValidator.validate(id);
        basketValidator.validate(basket);
        basketRepository.updateBasket(id, basket);
        return basket;
    }

    public void deleteBasket(long id) {
        basketValidator.validate(id);
        basketRepository.deleteBasket(id);
    }

    public Basket getBasket(long id) {
        basketValidator.validate(id);
        Optional<Basket> basket = basketRepository.getBasket(id);
        if (basket.isEmpty()) {
            throw new NotFoundException("Basket not found");
        } else {
            return basket.get();
        }
    }

    public Basket getBasketByPerson(long personId) {
        basketValidator.validate(personId);
        Optional<Basket> basket = basketRepository.getBasketByPerson(personId);
        if (basket.isEmpty()) {
            throw new NotFoundException("Basket not found");
        } else {
            return basket.get();
        }
    }
}
