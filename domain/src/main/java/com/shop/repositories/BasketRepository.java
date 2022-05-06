package com.shop.repositories;

import com.shop.dao.BasketDao;
import com.shop.models.Basket;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BasketRepository {
    private final BasketDao basketDao;

    public BasketRepository(BasketDao basketDao) {
        this.basketDao = basketDao;
    }

    public List<Basket> getBaskets() {
        return basketDao.getBaskets();
    }

    public void addBasket(Basket basket, long personId) {
        basketDao.addBasket(basket, personId);
    }

    public void updateBasket(long id, Basket basket) {
        basketDao.updateBasket(id, basket);
    }

    public void deleteBasket(long id) {
        basketDao.deleteBasket(id);
    }

    public Optional<Basket> getBasket(long id) {
        return basketDao.getBasket(id);
    }

    public Optional<Basket> getBasketByPerson(long personId) {
        return basketDao.getBasketByPerson(personId);
    }
}
