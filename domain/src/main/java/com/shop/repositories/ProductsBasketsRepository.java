package com.shop.repositories;

import com.shop.dao.ProductsBasketsDao;
import com.shop.models.Product;

import java.util.List;
import java.util.Optional;

public class ProductsBasketsRepository {
    private final ProductsBasketsDao productsBasketsDao;

    public ProductsBasketsRepository(ProductsBasketsDao productsBasketsDao) {
        this.productsBasketsDao = productsBasketsDao;
    }

    public List<Product> getProductsFromBasket(long idBasket) {
        return productsBasketsDao.getProductsFromBasket(idBasket);
    }

    public void addProduct(long idProduct, long idBasket) {
        productsBasketsDao.addProductToBasket(idProduct, idBasket);
    }

    public void deleteProduct(long idProduct, long idBasket) {
        productsBasketsDao.deleteProductFromBasket(idProduct, idBasket);
    }

    public Optional<Product> getProduct(long idProduct, long idBasket) {
        return productsBasketsDao.getProductFromBasket(idProduct, idBasket);
    }
}
