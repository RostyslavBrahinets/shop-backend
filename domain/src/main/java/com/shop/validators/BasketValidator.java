package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;

public class BasketValidator {
    private final BasketRepository basketRepository;

    public BasketValidator(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public void validate(Basket basket) {
        int totalCost = basket.getTotalCost();

        if (totalCost < 0) {
            throw new ValidationException("Total cost of products in basket is invalid");
        }
    }

    public void validate(int id) {
        if (id < 1 || id > basketRepository.getBaskets().size()) {
            throw new NotFoundException("Basket not found");
        }
    }
}