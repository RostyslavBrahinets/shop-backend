package com.shop.repositories;

import com.shop.dao.CartDao;
import com.shop.models.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartRepository {
    private final CartDao cartDao;

    public CartRepository(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    public List<Cart> findAll() {
        return cartDao.findAll();
    }

    public Optional<Cart> findById(long id) {
        return cartDao.findById(id);
    }

    public Optional<Cart> findByUser(long userId) {
        return cartDao.findByUser(userId);
    }

    public Cart save(double totalCost, long userId) {
        cartDao.save(totalCost, userId);
        return Cart.of(totalCost);
    }

    public Cart update(long id, double totalCost) {
        cartDao.update(id, totalCost);
        return Cart.of(totalCost).withId(id);
    }

    public void delete(long id) {
        cartDao.delete(id);
    }

    public int count() {
        return cartDao.findAll().size();
    }
}
