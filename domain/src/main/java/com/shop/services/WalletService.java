package com.shop.services;

import com.shop.exceptions.NotFoundException;
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

    public List<Wallet> getWallets() {
        return walletRepository.getWallets();
    }

    public Wallet addWallet(Wallet wallet, long personId) {
        walletValidator.validate(wallet);
        walletRepository.addWallet(wallet, personId);
        return wallet;
    }

    public Wallet updateWallet(long id, Wallet wallet) {
        walletValidator.validate(id);
        walletValidator.validate(wallet);
        walletRepository.updateWallet(id, wallet);
        return wallet;
    }

    public void deleteWallet(long id) {
        walletValidator.validate(id);
        walletRepository.deleteWallet(id);
    }

    public Wallet getWallet(long id) {
        walletValidator.validate(id);
        Optional<Wallet> wallet = walletRepository.getWallet(id);
        if (wallet.isEmpty()) {
            throw new NotFoundException("Wallet not found");
        } else {
            return wallet.get();
        }
    }

    public Wallet getWalletByPerson(long personId) {
        personValidator.validate(personId);
        Optional<Wallet> wallet = walletRepository.getWalletByPerson(personId);
        if (wallet.isEmpty()) {
            throw new NotFoundException("Wallet not found");
        } else {
            return wallet.get();
        }
    }
}
