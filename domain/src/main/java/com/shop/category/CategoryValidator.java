package com.shop.category;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.category.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryValidator {
    public void validateCategory(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name of category is invalid");
        }
    }

    public void validate(long id, List<Category> categories) {
        List<Long> ids = new ArrayList<>();

        for (Category category : categories) {
            ids.add(category.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Category not found");
        }
    }

    public void validate(String name, List<Category> categories) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name of category is invalid");
        }

        List<String> names = new ArrayList<>();

        for (Category category : categories) {
            names.add(category.getName());
        }

        if (!names.contains(name)) {
            throw new NotFoundException("Category not found");
        }
    }
}