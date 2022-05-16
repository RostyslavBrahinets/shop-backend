package com.shop.mvc;

import com.shop.models.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @GetMapping("/products/add")
    public String add(Model model) {
        model.addAttribute("categories", Category.values());
        return "products/add";
    }

    @GetMapping("/products/delete")
    public String delete() {
        return "products/delete";
    }
}
