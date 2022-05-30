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

    public List<Product> findAllProductsInCategory(long categoryId) {
        return productCategoryDao.findAllProductsInCategory(categoryId);
    }

    public Optional<Category> findCategoryForProduct(long productId) {
        return productCategoryDao.findCategoryForProduct(productId);
    }

    public void saveProductToCategory(long productId, long categoryId) {
        productCategoryDao.saveProductToCategory(productId, categoryId);
    }

    public void deleteProductFromCategory(long productId, long categoryId) {
        productCategoryDao.deleteProductFromCategory(productId, categoryId);
    }
}
