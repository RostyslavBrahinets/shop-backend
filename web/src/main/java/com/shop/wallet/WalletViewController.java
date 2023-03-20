package com.shop.wallet;

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
        Wallet wallet = walletService.findByUser(User.of(null, null).withId(user.getId()));
        model.addAttribute("id", wallet.getId());
        return "wallet/index";
    }
}
