package com.shop.mvc;

import com.shop.models.Category;
import com.shop.models.Person;
import com.shop.services.PersonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final PersonService personService;

    public AdminViewController(PersonService personService) {
        this.personService = personService;
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
        @PathVariable String id,
        Model model
    ) {
        model.addAttribute("id", id);
        return "admin/get";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        model.addAttribute("categories", Category.values());
        return "products/add";
    }

    @GetMapping("/products/delete")
    public String deleteProduct() {
        return "products/delete";
    }
}
