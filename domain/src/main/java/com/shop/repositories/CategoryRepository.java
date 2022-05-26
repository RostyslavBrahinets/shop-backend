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

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public Optional<Category> findById(long id) {
        return categoryDao.findById(id);
    }

    public Optional<Category> findByName(String name) {
        return categoryDao.findByName(name);
    }

    public Category save(Category category) {
        categoryDao.save(category.getName());
        return category;
    }

    public void delete(String name) {
        categoryDao.delete(name);
    }

    public int count() {
        return categoryDao.findAll().size();
    }
}
