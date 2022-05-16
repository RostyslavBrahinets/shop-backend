package com.shop.mvc;

import com.shop.models.Person;
import com.shop.services.PersonService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {
    private final PersonService personService;

    public MainViewController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        if (userDetails != null) {
            Person person = personService.getPerson(userDetails.getUsername());
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

            model.addAttribute("id", person.getId());
            model.addAttribute("role", role.getAuthority());
        } else {
            model.addAttribute("id", 0);
            model.addAttribute("role", "ROLE_GUEST");
        }

        return "index";
    }
}

