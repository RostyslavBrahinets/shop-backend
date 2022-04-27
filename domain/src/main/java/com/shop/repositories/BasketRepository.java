package com.shop.repositories;

import com.shop.dao.BasketDao;
import com.shop.models.Basket;

import java.util.List;
import java.util.Optional;

public class BasketRepository {
    private final BasketDao basketDao;

    public BasketRepository(BasketDao basketDao) {
        this.basketDao = basketDao;
    }

    public List<Basket> getBaskets() {
        return basketDao.getBaskets();
    }

    public void addBasket(Basket basket, int personId) {
        basketDao.addBasket(basket, personId);
    }

    public void deleteBasket(int id) {
        basketDao.deleteBasket(id);
    }

    public Optional<Basket> getBasket(int id) {
        return basketDao.getBasket(id);
    }
}
