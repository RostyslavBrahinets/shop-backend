package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Basket;
import com.shop.repositories.BasketRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasketValidator {
    private final BasketRepository basketRepository;

    public BasketValidator(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public void validate(Basket basket) {
        double totalCost = basket.getTotalCost();

        if (totalCost < 0) {
            throw new ValidationException("Total cost of products in basket is invalid");
        }
    }

    public void validate(long id) {
        List<Long> ids = new ArrayList<>();

        for (Basket basket : basketRepository.getBaskets()) {
            ids.add(basket.getId());
        }

        if (id < 1 || !ids.contains(id)) {
            throw new NotFoundException("Basket not found");
        }
    }
}