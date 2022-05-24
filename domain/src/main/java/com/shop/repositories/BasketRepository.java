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

    public List<Basket> findAll() {
        return basketDao.findAll();
    }

    public void save(Basket basket, long personId) {
        basketDao.save(basket.getTotalCost(), personId);
    }

    public void update(long id, Basket basket) {
        basketDao.update(id, basket.getTotalCost());
    }

    public void delete(long id) {
        basketDao.delete(id);
    }

    public Optional<Basket> findById(long id) {
        return basketDao.findById(id);
    }

    public Optional<Basket> findByPerson(long personId) {
        return basketDao.findByPerson(personId);
    }

    public int count() {
        return basketDao.findAll().size();
    }
}
