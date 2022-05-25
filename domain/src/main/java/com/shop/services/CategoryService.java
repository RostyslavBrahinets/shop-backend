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
        categoryValidator.validate(id);
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseGet(Category::new);
    }

    public Category findByName(String name) {
        categoryValidator.validate(name);
        Optional<Category> category = categoryRepository.findByName(name);
        return category.orElseGet(Category::new);
    }

    public Category save(Category category) {
        categoryValidator.validate(category);
        categoryRepository.save(category);
        return category;
    }

    public void delete(String name) {
        categoryValidator.validate(name);
        categoryRepository.delete(name);
    }
}
