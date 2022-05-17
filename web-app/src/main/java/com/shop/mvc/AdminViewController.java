package com.shop.mvc;

import com.shop.models.Category;
import com.shop.models.Person;
import com.shop.services.CategoryService;
import com.shop.services.PersonService;
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
    private final PersonService personService;
    private final CategoryService categoryService;

    public AdminViewController(
        PersonService personService,
        CategoryService categoryService
    ) {
        this.personService = personService;
        this.categoryService = categoryService;
    }

    @GetMapping("/users")
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        Person person = personService.getPerson(userDetails.getUsername());
        model.addAttribute("id", person.getId());
        return "admin/index";
    }

    @GetMapping("/users/{id}")
    public String getUser(
        @PathVariable long id,
        Model model
    ) {
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
    public String addProduct(Model model) {
        List<String> categories = new ArrayList<>();

        for (Category category : categoryService.getCategories()) {
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
    public String addCategory() {
        return "categories/add";
    }

    @GetMapping("/categories/delete")
    public String deleteCategory() {
        return "categories/delete";
    }
}
