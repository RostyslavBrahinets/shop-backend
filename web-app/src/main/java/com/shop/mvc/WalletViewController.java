package com.shop.mvc;

import com.shop.models.Person;
import com.shop.models.Wallet;
import com.shop.services.PersonService;
import com.shop.services.WalletService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet")
public class WalletViewController {
    private final PersonService personService;
    private final WalletService walletService;

    public WalletViewController(
        PersonService personService,
        WalletService walletService
    ) {
        this.personService = personService;
        this.walletService = walletService;
    }

    @GetMapping()
    public String index(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);

        if (role.getAuthority().equals("ROLE_ADMIN")) {
            return "redirect:/";
        }

        Person person = personService.findByEmail(userDetails.getUsername());
        Wallet wallet = walletService.findByPerson(person.getId());
        model.addAttribute("id", wallet.getId());
        return "wallet/index";
    }
}
