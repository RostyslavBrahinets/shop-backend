package com.shop.main;

import com.shop.user.User;
import com.shop.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {
    private final UserService userService;

    public MainViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

            model.addAttribute("id", user.getId());
            model.addAttribute("role", role.getAuthority());
        } else {
            model.addAttribute("id", 0L);
            model.addAttribute("role", "ROLE_GUEST");
        }

        return "index";
    }
}
