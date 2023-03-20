package com.shop.signup;

import com.shop.adminnumber.AdminNumber;
import com.shop.adminnumber.AdminNumberService;
import com.shop.cart.Cart;
import com.shop.cart.CartService;
import com.shop.stripe.StripePayment;
import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.userrole.UserRoleService;
import com.shop.wallet.Wallet;
import com.shop.wallet.WalletService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignUpService {
    private final SignUpValidator signUpValidator;
    private final UserService userService;
    private final AdminNumberService adminNumberService;
    private final UserRoleService userRoleService;
    private final CartService cartService;
    private final WalletService walletService;
    private final StripePayment stripePayment;

    public SignUpService(
        SignUpValidator signUpValidator,
        UserService userService,
        AdminNumberService adminNumberService,
        UserRoleService userRoleService,
        CartService cartService,
        WalletService walletService,
        StripePayment stripePayment
    ) {
        this.signUpValidator = signUpValidator;
        this.userService = userService;
        this.adminNumberService = adminNumberService;
        this.userRoleService = userRoleService;
        this.cartService = cartService;
        this.walletService = walletService;
        this.stripePayment = stripePayment;
    }

    public void signUp(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        String adminNumber
    ) throws StripeException {
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

        long adminNumberId = findAdminNumberId(adminNumber);

        if (validData) {
            userService.save(
                User.of(
                    firstName,
                    lastName,
                    email,
                    phone,
                    password,
                    adminNumberId
                )
            );

            userRoleService.saveRoleForUser(findUserId(), 2);
            cartService.save(Cart.of(0, findUserId()));
            saveWalletForUser(findUserId());
        }
    }

    private long findAdminNumberId(String adminNumber) {
        List<AdminNumber> adminNumbers = adminNumberService.findAll();

        for (AdminNumber number : adminNumbers) {
            if (number.getNumber().equals(adminNumber)) {
                return number.getId();
            }
        }

        return 0;
    }

    private long findUserId() {
        List<User> users = userService.findAll();
        return users.get(users.size() - 1).getId();
    }

    private void saveWalletForUser(long userId) throws StripeException {
        Optional<Customer> customer = stripePayment
            .saveCustomer(userService.findById(userId));
        if (customer.isPresent()) {
            Wallet wallet = new Wallet();
            wallet.setNumber(customer.get().getId());
            wallet.setAmountOfMoney(customer.get().getBalance());
            walletService.save(
                Wallet.of(
                    wallet.getNumber(),
                    wallet.getAmountOfMoney(),
                    userId
                )
            );
        }
    }
}
