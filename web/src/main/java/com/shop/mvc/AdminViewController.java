package com.shop.mvc;

import com.shop.category.Category;
import com.shop.user.User;
import com.shop.category.CategoryService;
import com.shop.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final UserService userService;
    private final CategoryService categoryService;

    public AdminViewController(
        UserService userService,
        CategoryService categoryService
    ) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/users")
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("id", user.getId());
        return "admin/index";
    }

    @GetMapping("/users/{id}")
    public String getUser(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable long id,
        Model model
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("currentId", user.getId());
        model.addAttribute("id", id);
        return "admin/find";
    }

    @GetMapping("/users/{id}/update-role")
    public String updateRoleForUser(
        @PathVariable long id,
        Model model
    ) {
        model.addAttribute("id", id);
        return "admin/update_role";
    }

    @GetMapping("/products/add")
    public String saveProduct(Model model) {
        List<String> categories = new ArrayList<>();

        for (Category category : categoryService.findAll()) {
            categories.add(category.getName());
        }

        model.addAttribute("categories", categories);
        return "products/add";
    }

    @GetMapping("/products/delete")
    public String deleteProduct() {
        return "products/delete";
    }

    @GetMapping("/categories/add")
    public String saveCategory() {
        return "categories/add";
    }

    @GetMapping("/categories/delete")
    public String deleteCategory() {
        return "categories/delete";
    }
}
