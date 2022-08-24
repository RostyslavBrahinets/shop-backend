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
    private final PersonService personService;
    private final PersonValidator personValidator;

    public BasketService(
        BasketRepository basketRepository,
        BasketValidator basketValidator,
        PersonService personService,
        PersonValidator personValidator
    ) {
        this.basketRepository = basketRepository;
        this.basketValidator = basketValidator;
        this.personService = personService;
        this.personValidator = personValidator;
    }

    public List<Basket> findAll() {
        return basketRepository.findAll();
    }

    public Basket findById(long id) {
        basketValidator.validate(id);
        Optional<Basket> basket = basketRepository.findById(id);
        return basket.orElseGet(Basket::new);
    }

    public Basket findByPerson(long personId) {
        personValidator.validate(personId, personService.findAll());
        Optional<Basket> basket = basketRepository.findByPerson(personId);
        return basket.orElseGet(Basket::new);
    }

    public Basket save(double totalCost, long personId) {
        basketValidator.validate(totalCost);
        personValidator.validate(personId, personService.findAll());
        return basketRepository.save(totalCost, personId);
    }

    public Basket update(long id, double totalCost) {
        basketValidator.validate(id);
        basketValidator.validate(totalCost);
        return basketRepository.update(id, totalCost);
    }

    public void delete(long id) {
        basketValidator.validate(id);
        basketRepository.delete(id);
    }
}
