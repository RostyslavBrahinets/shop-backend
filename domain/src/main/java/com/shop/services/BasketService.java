package com.shop.services;

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

    public List<Basket> findAll() {
        return basketRepository.findAll();
    }

    public Basket save(Basket basket, long personId) {
        basketValidator.validate(basket);
        personValidator.validate(personId);
        basketRepository.save(basket, personId);
        return basket;
    }

    public Basket update(long id, Basket basket) {
        basketValidator.validate(id);
        basketValidator.validate(basket);
        basketRepository.update(id, basket);
        return basket;
    }

    public void delete(long id) {
        basketValidator.validate(id);
        basketRepository.delete(id);
    }

    public Basket findById(long id) {
        basketValidator.validate(id);
        Optional<Basket> basketOptional = basketRepository.findById(id);
        return basketOptional.orElseGet(Basket::new);
    }

    public Basket findByPerson(long personId) {
        personValidator.validate(personId);
        Optional<Basket> basketOptional = basketRepository.findByPerson(personId);
        return basketOptional.orElseGet(Basket::new);
    }
}
