package com.shop.wallet;

import com.shop.stripe.StripePayment;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(WalletController.WALLETS_URL)
public class WalletController {
    public static final String WALLETS_URL = "/api/wallets";
    private final WalletService walletService;
    private final StripePayment stripePayment;

    public WalletController(
        WalletService walletService,
        StripePayment stripePayment
    ) {
        this.walletService = walletService;
        this.stripePayment = stripePayment;
    }

    @GetMapping
    public List<Wallet> findAll() {
        return walletService.findAll();
    }

    @GetMapping("/{id}")
    public Wallet findById(@PathVariable long id) throws StripeException {
        Wallet wallet = walletService.findById(id);
        Optional<Customer> customer = stripePayment
            .findByIdCustomer(walletService.findById(id).getNumber());

        Wallet updatedWallet = null;
        if (customer.isPresent()) {
            long balance = customer.get().getBalance();
            double amountOfMoney = Double.parseDouble(
                String.format("%d.%d", balance / -100, balance % 100 * -1)
            );

            wallet.setAmountOfMoney(amountOfMoney);
            updatedWallet = walletService.update(
                Wallet.of(
                        wallet.getNumber(),
                        wallet.getAmountOfMoney(),
                        wallet.getUserId()
                    )
                    .withId(wallet.getId())
            );
        }

        return updatedWallet;
    }

    @PostMapping
    public Wallet save(
        @RequestBody Wallet wallet
    ) {
        return walletService.save(
            Wallet.of(
                wallet.getNumber(),
                wallet.getAmountOfMoney(),
                wallet.getUserId()
            )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        walletService.delete(Wallet.of(null, 0, 0).withId(id));
    }
}
