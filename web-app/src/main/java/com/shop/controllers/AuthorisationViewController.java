package com.shop.controllers;

import com.shop.models.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.shop.SessionAttributes.USER_ROLE_SESSION_PARAMETER;

@Controller
public class AuthorisationViewController {
    @GetMapping("/authorisation")
    public String authorisation() {
        return "authorisation";
    }

    @PostMapping("/authorisation")
    public String logIn(
        @ModelAttribute("userRole") String userRole,
        HttpSession session
    ) {
        List<Role> roles = List.of(Role.values());
        if (roles.contains(Role.valueOf(userRole.toUpperCase()))) {
            session.setAttribute(USER_ROLE_SESSION_PARAMETER, userRole);
            return "redirect:/";
        } else {
            return "redirect:/authorisation";
        }
    }
}
