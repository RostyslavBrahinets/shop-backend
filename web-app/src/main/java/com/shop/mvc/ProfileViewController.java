package com.shop.mvc;

import com.shop.models.Person;
import com.shop.services.PersonService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileViewController {
    private final PersonService personService;

    public ProfileViewController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        Person person = personService.findByEmail(userDetails.getUsername());
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

        model.addAttribute("id", person.getId());
        model.addAttribute("role", role.getAuthority());

        return "profile/index";
    }

    @GetMapping("/update")
    public String update(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        Person person = personService.findByEmail(userDetails.getUsername());
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

        model.addAttribute("id", person.getId());
        model.addAttribute("role", role.getAuthority());

        return "profile/update";
    }
}
