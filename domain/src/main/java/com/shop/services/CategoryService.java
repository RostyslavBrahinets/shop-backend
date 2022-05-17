package com.shop.services;

import com.shop.exceptions.NotFoundException;
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


    public List<Category> getCategories() {
        return categoryRepository.getCategories();
    }

    public Category addCategory(Category category) {
        categoryValidator.validate(category);
        categoryRepository.addCategory(category);
        return category;
    }

    public void deleteCategory(long id) {
        categoryValidator.validate(id);
        categoryRepository.deleteCategory(id);
    }

    public Category getCategory(long id) {
        categoryValidator.validate(id);
        Optional<Category> category = categoryRepository.getCategory(id);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found");
        } else {
            return category.get();
        }
    }

    public Category getCategory(String name) {
        categoryValidator.validate(name);
        Optional<Category> category = categoryRepository.getCategory(name);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found");
        } else {
            return category.get();
        }
    }
}
