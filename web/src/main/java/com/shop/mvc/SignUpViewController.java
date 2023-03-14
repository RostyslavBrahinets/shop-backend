package com.shop.mvc;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpViewController {
    @GetMapping("/sign-up")
    public String signUp(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);
            String authority = role.getAuthority();
            boolean alreadyLoggedIn = authority.equals("ROLE_ADMIN")
                || authority.equals("ROLE_USER");

            if (alreadyLoggedIn) {
                return "redirect:/";
            }
        }

        return "sign-up";
    }
}
