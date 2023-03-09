package com.shop.mvc;

import com.shop.models.Cart;
import com.shop.models.Person;
import com.shop.services.CartService;
import com.shop.services.PersonService;
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
    private final PersonService personService;
    private final CartService cartService;

    public CartViewController(
        PersonService personService,
        CartService cartService
    ) {
        this.personService = personService;
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

        Person person = personService.findByEmail(userDetails.getUsername());
        Cart cart = cartService.findByPerson(person.getId());
        model.addAttribute("id", cart.getId());
        return "cart/index";
    }
}
