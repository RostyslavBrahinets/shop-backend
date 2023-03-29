package com.shop.category;

import com.shop.interfaces.ServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ServiceInterface<Category> {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public CategoryService(
        CategoryRepository categoryRepository,
        CategoryValidator categoryValidator
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(long id) {
        categoryValidator.validate(id, categoryRepository.findAll());
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseGet(Category::new);
    }

    @Override
    public Category save(Category category) {
        categoryValidator.validateCategory(category.getName());
        categoryRepository.save(category);
        category.setId(categoryRepository.findAll().size() + 1);
        return category;
    }

    @Override
    public Category update(long id, Category category) {
        categoryValidator.validate(id, findAll());
        categoryValidator.validateCategory(category.getName());
        categoryRepository.update(id, category);
        return category;
    }

    @Override
    public void delete(Category category) {
        categoryValidator.validate(category.getName(), categoryRepository.findAll());
        categoryRepository.delete(category);
    }

    public Category findByName(String name) {
        categoryValidator.validate(name, categoryRepository.findAll());
        Optional<Category> category = categoryRepository.findByName(name);
        return category.orElseGet(Category::new);
    }
}
