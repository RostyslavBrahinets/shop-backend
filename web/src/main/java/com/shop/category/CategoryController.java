package com.shop.category;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(CategoryController.CATEGORIES_URL)
public class CategoryController {
    public static final String CATEGORIES_URL = "/api/v1/categories";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable long id) {
        return categoryService.findById(id);
    }

    @PostMapping("")
    public Category save(@RequestBody Category category) {
        return categoryService.save(Category.of(category.getName()));
    }

    @PutMapping("/{id}")
    public Category update(
        @PathVariable long id,
        @RequestBody Category category
    ) {
        return categoryService.update(id, category);
    }

    @DeleteMapping("/{name}")
    public void delete(@PathVariable String name) {
        categoryService.delete(Category.of(name));
    }
}
