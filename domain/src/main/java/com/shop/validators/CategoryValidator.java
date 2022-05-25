package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Category;
import com.shop.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public CategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validate(Category category) {
        String name = category.getName();

        if (name == null || name.isBlank()) {
            throw new ValidationException("Name of category is invalid");
        }
    }

    public void validate(long id) {
        List<Long> ids = new ArrayList<>();

        for (Category category : categoryRepository.findAll()) {
            ids.add(category.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Category not found");
        }
    }

    public void validate(String name) {
        List<String> names = new ArrayList<>();

        for (Category category : categoryRepository.findAll()) {
            names.add(category.getName());
        }

        if (!names.contains(name)) {
            throw new NotFoundException("Category not found");
        }
    }
}