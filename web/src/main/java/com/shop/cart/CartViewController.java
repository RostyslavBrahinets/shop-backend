package com.shop.cart;

import com.shop.user.User;
import com.shop.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartViewController {
    private final UserService userService;
    private final CartService cartService;

    public CartViewController(
        UserService userService,
        CartService cartService
    ) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

        if (role.getAuthority().equals("ROLE_ADMIN")) {
            return "redirect:/";
        }

        User user = userService.findByEmail(userDetails.getUsername());
        Cart cart = cartService.findByUser(User.of(null, null).withId(user.getId()));
        model.addAttribute("id", cart.getId());
        return "cart/index";
    }
}
