package com.shop.mvc;

import com.shop.models.User;
import com.shop.models.Wallet;
import com.shop.services.UserService;
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
    private final UserService userService;
    private final WalletService walletService;

    public WalletViewController(
        UserService userService,
        WalletService walletService
    ) {
        this.userService = userService;
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

        User user = userService.findByEmail(userDetails.getUsername());
        Wallet wallet = walletService.findByUser(user.getId());
        model.addAttribute("id", wallet.getId());
        return "wallet/index";
    }
}
