package com.shop.services;

import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.validators.UserValidator;
import com.shop.validators.WalletValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletValidator walletValidator;
    private final UserService userService;
    private final UserValidator userValidator;

    public WalletService(
        WalletRepository walletRepository,
        WalletValidator walletValidator,
        UserService userService,
        UserValidator userValidator
    ) {
        this.walletRepository = walletRepository;
        this.walletValidator = walletValidator;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    public Wallet findById(long id) {
        walletValidator.validate(id, walletRepository.findAll());
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.orElseGet(Wallet::new);
    }

    public Wallet findByUser(long userId) {
        userValidator.validate(userId, userService.findAll());
        Optional<Wallet> wallet = walletRepository.findByUser(userId);
        return wallet.orElseGet(Wallet::new);
    }

    public Wallet save(
        String number,
        double amountOfMoney,
        long userId
    ) {
        walletValidator.validate(number, amountOfMoney);
        userValidator.validate(userId, userService.findAll());
        return walletRepository.save(number, amountOfMoney, userId);
    }

    public Wallet update(long id, double amountOfMoney) {
        walletValidator.validate(id, walletRepository.findAll());
        walletValidator.validateAmountOfMoney(amountOfMoney);
        return walletRepository.update(id, amountOfMoney);
    }

    public void delete(long id) {
        walletValidator.validate(id, walletRepository.findAll());
        walletRepository.delete(id);
    }
}
