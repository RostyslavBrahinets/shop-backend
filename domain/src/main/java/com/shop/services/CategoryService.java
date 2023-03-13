package com.shop.services;

import com.shop.models.Category;
import com.shop.repositories.CategoryRepository;
import com.shop.validators.CategoryValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public CategoryService(
        CategoryRepository categoryRepository,
        CategoryValidator categoryValidator
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(long id) {
        categoryValidator.validate(id, categoryRepository.findAll());
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseGet(Category::new);
    }

    public Category findByName(String name) {
        categoryValidator.validate(name, categoryRepository.findAll());
        Optional<Category> category = categoryRepository.findByName(name);
        return category.orElseGet(Category::new);
    }

    public Category save(String name) {
        categoryValidator.validateCategory(name);
        categoryRepository.save(name);
        return Category.of(name).withId(categoryRepository.findAll().size() + 1);
    }

    public void delete(String name) {
        categoryValidator.validate(name, categoryRepository.findAll());
        categoryRepository.delete(name);
    }
}
