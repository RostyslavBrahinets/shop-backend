package com.shop.repositories;

import com.shop.dao.ProductCategoryDao;
import com.shop.models.Category;
import com.shop.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductCategoryRepository {
    private final ProductCategoryDao productCategoryDao;

    public ProductCategoryRepository(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    public Optional<Category> getCategoryForProduct(long productId) {
        return productCategoryDao.getCategoryForProduct(productId);
    }

    public void addProductToCategory(long productId, long categoryId) {
        productCategoryDao.addProductToCategory(productId, categoryId);
    }

    public List<Product> getProductsInCategory(long categoryId) {
        return productCategoryDao.getProductsInCategory(categoryId);
    }
}
