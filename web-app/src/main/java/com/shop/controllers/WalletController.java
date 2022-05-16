package com.shop.controllers;

import com.shop.models.Wallet;
import com.shop.services.WalletService;
import com.shop.stripe.StripePayment;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = WalletController.WALLETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class WalletController {
    public static final String WALLETS_URL = "/web-api/wallets";
    private final WalletService walletService;
    private final StripePayment stripePayment;

    public WalletController(WalletService walletService, StripePayment stripePayment) {
        this.walletService = walletService;
        this.stripePayment = stripePayment;
    }

    @GetMapping
    public List<Wallet> findAllWallets() {
        return walletService.getWallets();
    }

    @GetMapping("/{id}")
    public Wallet findByIdWallet(@PathVariable int id) throws StripeException {
        Wallet wallet = walletService.getWallet(id);
        Optional<Customer> customer = stripePayment
            .findByIdCustomer(walletService.getWallet(id).getNumber());

        customer.ifPresent(value -> {
            wallet.setAmountOfMoney(value.getBalance() / -100.0);
            walletService.updateWallet(wallet.getId(), wallet);
        });

        return wallet;
    }

    @PostMapping
    public Wallet saveWallet(
        @RequestBody Wallet wallet,
        @RequestBody int personId
    ) {
        return walletService.addWallet(wallet, personId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWallet(@PathVariable int id) {
        walletService.deleteWallet(id);
    }
}
