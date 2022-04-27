package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.validators.WalletValidator;

import java.util.List;
import java.util.Optional;

public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletValidator validator;

    public WalletService(
        WalletRepository walletRepository,
        WalletValidator validator
    ) {
        this.walletRepository = walletRepository;
        this.validator = validator;
    }

    public List<Wallet> getWallets() {
        return walletRepository.getWallets();
    }

    public Wallet addWallet(Wallet wallet, int personId) {
        validator.validate(wallet);
        walletRepository.addWallet(wallet, personId);
        return wallet;
    }

    public Wallet updateWallet(int id, Wallet wallet) {
        validator.validate(id);
        validator.validate(wallet);
        walletRepository.updateWallet(id, wallet);
        return wallet;
    }

    public void deleteWallet(int id) {
        validator.validate(id);
        walletRepository.deleteWallet(id);
    }

    public Wallet getWallet(int id) {
        validator.validate(id);
        Optional<Wallet> wallet = walletRepository.getWallet(id);
        if (wallet.isEmpty()) {
            throw new NotFoundException("Wallet not found");
        } else {
            return wallet.get();
        }
    }
}
