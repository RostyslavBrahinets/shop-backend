package com.shop.repositories;

import com.shop.dao.CategoryDao;
import com.shop.models.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepository(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> getCategories() {
        return categoryDao.getCategories();
    }

    public void addCategory(Category category) {
        categoryDao.addCategory(category);
    }

    public void deleteCategory(long id) {
        categoryDao.deleteCategory(id);
    }

    public Optional<Category> getCategory(long id) {
        return categoryDao.getCategory(id);
    }

    public Optional<Category> getCategory(String name) {
        return categoryDao.getCategory(name);
    }
}
