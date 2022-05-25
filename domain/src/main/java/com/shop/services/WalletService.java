package com.shop.services;

import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.validators.PersonValidator;
import com.shop.validators.WalletValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletValidator walletValidator;
    private final PersonValidator personValidator;

    public WalletService(
        WalletRepository walletRepository,
        WalletValidator walletValidator,
        PersonValidator personValidator
    ) {
        this.walletRepository = walletRepository;
        this.walletValidator = walletValidator;
        this.personValidator = personValidator;
    }

    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    public Wallet findById(long id) {
        walletValidator.validate(id);
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.orElseGet(Wallet::new);
    }

    public Wallet findByPerson(long personId) {
        personValidator.validate(personId);
        Optional<Wallet> wallet = walletRepository.findByPerson(personId);
        return wallet.orElseGet(Wallet::new);
    }

    public Wallet save(Wallet wallet, long personId) {
        walletValidator.validate(wallet);
        walletRepository.save(wallet, personId);
        return wallet;
    }

    public Wallet update(long id, Wallet wallet) {
        walletValidator.validate(id);
        walletValidator.validate(wallet);
        walletRepository.update(id, wallet);
        return wallet;
    }

    public void delete(long id) {
        walletValidator.validate(id);
        walletRepository.delete(id);
    }
}
