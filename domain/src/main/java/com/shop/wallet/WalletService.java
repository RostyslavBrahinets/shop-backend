package com.shop.wallet;

import com.shop.interfaces.ServiceInterface;
import com.shop.user.User;
import com.shop.user.UserService;
import com.shop.user.UserValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService implements ServiceInterface<Wallet> {
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

    @Override
    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Override
    public Wallet findById(long id) {
        walletValidator.validate(id, walletRepository.findAll());
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.orElseGet(Wallet::new);
    }

    @Override
    public Wallet save(Wallet wallet) {
        walletValidator.validate(wallet.getNumber(), wallet.getAmountOfMoney());
        userValidator.validate(wallet.getUserId(), userService.findAll());
        walletRepository.save(wallet.getNumber(), wallet.getAmountOfMoney(), wallet.getUserId());
        wallet.setId(walletRepository.findAll().size() + 1);
        return wallet;
    }

    @Override
    public Wallet update(Wallet wallet) {
        walletValidator.validate(wallet.getId(), walletRepository.findAll());
        walletValidator.validateAmountOfMoney(wallet.getAmountOfMoney());
        walletRepository.update(wallet.getId(), wallet.getAmountOfMoney());

        Optional<Wallet> walletOptional = walletRepository.findById(wallet.getId());
        String number = "";
        if (walletOptional.isPresent()) {
            number = walletOptional.get().getNumber();
        }
        wallet.setNumber(number);

        return wallet;
    }

    @Override
    public void delete(Wallet wallet) {
        walletValidator.validate(wallet.getId(), walletRepository.findAll());
        walletRepository.delete(wallet.getId());
    }

    public Wallet findByUser(User user) {
        userValidator.validate(user.getId(), userService.findAll());
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        return wallet.orElseGet(Wallet::new);
    }
}
