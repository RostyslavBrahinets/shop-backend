package com.shop.wallet;

import com.shop.user.UserService;
import com.shop.wallet.Wallet;
import com.shop.wallet.WalletRepository;
import com.shop.user.UserValidator;
import com.shop.wallet.WalletValidator;
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
        walletRepository.save(number, amountOfMoney, userId);
        return Wallet.of(number, amountOfMoney).withId(walletRepository.findAll().size() + 1);
    }

    public Wallet update(long id, double amountOfMoney) {
        walletValidator.validate(id, walletRepository.findAll());
        walletValidator.validateAmountOfMoney(amountOfMoney);
        walletRepository.update(id, amountOfMoney);

        Optional<Wallet> wallet = walletRepository.findById(id);
        String number = "";
        if (wallet.isPresent()) {
            number = wallet.get().getNumber();
        }

        return Wallet.of(number, amountOfMoney).withId(id);
    }

    public void delete(long id) {
        walletValidator.validate(id, walletRepository.findAll());
        walletRepository.delete(id);
    }
}
