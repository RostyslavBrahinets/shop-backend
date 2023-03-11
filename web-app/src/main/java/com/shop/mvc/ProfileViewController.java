package com.shop.mvc;

import com.shop.models.User;
import com.shop.services.UserService;
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
    private final UserService userService;

    public ProfileViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

        model.addAttribute("id", user.getId());
        model.addAttribute("role", role.getAuthority());

        return "profile/index";
    }

    @GetMapping("/update")
    public String update(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

        model.addAttribute("id", user.getId());
        model.addAttribute("role", role.getAuthority());

        return "profile/update";
    }
}
