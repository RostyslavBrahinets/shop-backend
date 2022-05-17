package com.shop.controllers;

import com.shop.models.Category;
import com.shop.services.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = CategoryController.CATEGORIES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
    public static final String CATEGORIES_URL = "/web-api/categories";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> findAllCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    public Category findByIdCategory(@PathVariable long id) {
        return categoryService.getCategory(id);
    }

    @PostMapping
    public Category saveCategory(
        @RequestBody Category category,
        HttpServletResponse response
    ) throws IOException {
        response.sendRedirect("/categories");
        return categoryService.addCategory(category);
    }

    @PostMapping("/{id}")
    public void deleteCategory(
        @PathVariable int id,
        HttpServletResponse response
    ) throws IOException {
        categoryService.deleteCategory(id);
        response.sendRedirect("/categories");
    }
}
