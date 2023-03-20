package com.shop.category;

import com.shop.category.Category;
import com.shop.category.CategoryRepository;
import com.shop.category.CategoryValidator;
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

    public Category save(Category category) {
        categoryValidator.validateCategory(category.getName());
        categoryRepository.save(category.getName());
        return Category.of(category.getName())
            .withId(categoryRepository.findAll().size() + 1);
    }

    public void delete(Category category) {
        categoryValidator.validate(category.getName(), categoryRepository.findAll());
        categoryRepository.delete(category.getName());
    }
}
