package com.shop.repositories;

import com.shop.dao.ProductsCartsDao;
import com.shop.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductsCartsRepository {
    private final ProductsCartsDao productsCartsDao;

    public ProductsCartsRepository(ProductsCartsDao productsCartsDao) {
        this.productsCartsDao = productsCartsDao;
    }

    public List<Product> findAllProductsInCart(long cartId) {
        return productsCartsDao.findAllProductsInCart(cartId);
    }

    public void saveProductToCart(long productId, long cartId) {
        productsCartsDao.saveProductToCart(productId, cartId);
    }

    public void deleteProductFromCart(long productId, long cartId) {
        productsCartsDao.deleteProductFromCart(productId, cartId);
    }

    public void deleteProductsFromCart(long cartId) {
        productsCartsDao.deleteProductsFromCart(cartId);
    }
}
