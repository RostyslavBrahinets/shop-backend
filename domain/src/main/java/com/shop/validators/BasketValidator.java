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
    public void validate(double totalCost) {
        if (totalCost < 0) {
            throw new ValidationException("Total cost of products in basket is invalid");
        }
    }

    public void validate(long id, List<Basket> baskets) {
        List<Long> ids = new ArrayList<>();

        for (Basket basket : baskets) {
            ids.add(basket.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Basket not found");
        }
    }
}