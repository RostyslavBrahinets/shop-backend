//package com.shop.mvc;
//
//import com.shop.models.Role;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.servlet.http.HttpSession;
//import java.util.List;
//
//import static com.shop.SessionAttributes.ROLE_SESSION_PARAMETER;
//
//@Controller
//public class AuthorisationController {
//    @GetMapping("/authorisation")
//    public String authorisation() {
//        return "authorisation";
//    }
//
//    @PostMapping("/authorisation")
//    public String logIn(
//        @ModelAttribute("role") String role,
//        HttpSession session
//    ) {
//        List<Role> roles = List.of(Role.values());
//        if (roles.contains(Role.valueOf(role.toUpperCase()))) {
//            session.setAttribute(ROLE_SESSION_PARAMETER, role);
//            return "redirect:/";
//        } else {
//            return "redirect:/authorisation";
//        }
//    }
//}
