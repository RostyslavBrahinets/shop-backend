package com.shop.signup;

import com.shop.adminnumber.AdminNumberService;
import com.shop.cart.Cart;
import com.shop.cart.CartService;
import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.userrole.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SignUpService {
    private final SignUpValidator signUpValidator;
    private final UserService userService;
    private final AdminNumberService adminNumberService;
    private final UserRoleService userRoleService;
    private final CartService cartService;

    public SignUpService(
        SignUpValidator signUpValidator,
        UserService userService,
        AdminNumberService adminNumberService,
        UserRoleService userRoleService,
        CartService cartService
    ) {
        this.signUpValidator = signUpValidator;
        this.userService = userService;
        this.adminNumberService = adminNumberService;
        this.userRoleService = userRoleService;
        this.cartService = cartService;
    }

    @Transactional
    public void signUp(
        String firstName,
        String lastName,
        String email,
        String phone,
        char[] password,
        String adminNumber
    ) {
        boolean validData = signUpValidator.isValidData(
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumber,
            userService.findAll(),
            adminNumberService.findAll()
        );

        if (validData) {
            userService.save(
                User.of(
                    firstName,
                    lastName,
                    email,
                    phone,
                    password,
                    adminNumber
                )
            );

            userRoleService.saveRoleForUser(findUserId(), adminNumber.isBlank() ? 2 : 1);

            if (adminNumber.isBlank()) {
                cartService.save(Cart.of(0, findUserId()));
            }
        }
    }

    private long findUserId() {
        List<User> users = userService.findAll();
        return users.get(users.size() - 1).getId();
    }
}
