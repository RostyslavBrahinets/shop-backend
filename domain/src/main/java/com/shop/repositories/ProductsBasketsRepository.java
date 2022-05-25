package com.shop.repositories;

import com.shop.dao.ProductsBasketsDao;
import com.shop.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductsBasketsRepository {
    private final ProductsBasketsDao productsBasketsDao;

    public ProductsBasketsRepository(ProductsBasketsDao productsBasketsDao) {
        this.productsBasketsDao = productsBasketsDao;
    }

    public List<Product> findAllProductsInBasket(long basketId) {
        return productsBasketsDao.findAllProductsInBasket(basketId);
    }

    public void saveProductToBasket(long productId, long basketId) {
        productsBasketsDao.saveProductToBasket(productId, basketId);
    }

    public void deleteProductFromBasket(long productId, long basketId) {
        productsBasketsDao.deleteProductFromBasket(productId, basketId);
    }

    public void deleteProductsFromBasket(long basketId) {
        productsBasketsDao.deleteProductsFromBasket(basketId);
    }
}
