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

    public Optional<Basket> findById(long id) {
        return basketDao.findById(id);
    }

    public Optional<Basket> findByPerson(long personId) {
        return basketDao.findByPerson(personId);
    }

    public Basket save(double totalCost, long personId) {
        basketDao.save(totalCost, personId);
        return Basket.of(totalCost);
    }

    public Basket update(long id, double totalCost) {
        basketDao.update(id, totalCost);
        return Basket.of(totalCost).withId(id);
    }

    public void delete(long id) {
        basketDao.delete(id);
    }

    public int count() {
        return basketDao.findAll().size();
    }
}
