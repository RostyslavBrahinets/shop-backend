package com.shop.controllers;

import com.shop.configs.AppConfig;
import com.shop.models.Wallet;
import com.shop.services.WalletService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = WalletController.WALLETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class WalletController {
    public static final String WALLETS_URL = "/web-api/wallets";
    public static final AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext(AppConfig.class);
    public static final WalletService walletService =
        applicationContext.getBean(WalletService.class);

    @GetMapping
    public List<Wallet> findAllWallets() {
        return walletService.getWallets();
    }

    @GetMapping("/{id}")
    public Wallet findByIdWallet(@PathVariable int id) {
        return walletService.getWallet(id);
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
