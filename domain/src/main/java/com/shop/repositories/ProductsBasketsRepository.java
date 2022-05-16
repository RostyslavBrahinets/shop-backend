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

    public List<Product> getProductsFromBasket(long basketId) {
        return productsBasketsDao.getProductsFromBasket(basketId);
    }

    public void addProductToBasket(long productId, long basketId) {
        productsBasketsDao.addProductToBasket(productId, basketId);
    }

    public void deleteProductFromBasket(long productId, long basketId) {
        productsBasketsDao.deleteProductFromBasket(productId, basketId);
    }

    public void deleteProductsFromBasket(long basketId) {
        productsBasketsDao.deleteProductsFromBasket(basketId);
    }
}
