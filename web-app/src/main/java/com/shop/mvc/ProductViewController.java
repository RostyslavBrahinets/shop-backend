package com.shop.mvc;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {
    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        if (userDetails != null) {
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);
            model.addAttribute("role", role.getAuthority());
        } else {
            model.addAttribute("role", "ROLE_GUEST");
        }

        return "products/index";
    }

    @GetMapping("/{id}")
    public String get(
        @PathVariable long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        if (userDetails != null) {
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);
            model.addAttribute("role", role.getAuthority());
        } else {
            model.addAttribute("role", "ROLE_GUEST");
        }

        model.addAttribute("id", id);

        return "products/find";
    }
}
