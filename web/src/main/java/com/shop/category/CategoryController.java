package com.shop.category;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CategoryController.CATEGORIES_URL)
public class CategoryController {
    public static final String CATEGORIES_URL = "/web-api/categories";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> findAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category findByIdCategory(@PathVariable long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public Category saveCategory(@RequestBody Category category) {
        return categoryService.save(category.getName());
    }

    @PostMapping("/{name}")
    public String deleteCategory(@PathVariable String name) {
        categoryService.delete(name);
        return "Category Successfully Deleted";
    }
}
