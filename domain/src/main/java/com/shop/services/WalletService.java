package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import com.shop.validators.WalletValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

    public Wallet addWallet(Wallet wallet, long personId) {
        validator.validate(wallet);
        walletRepository.addWallet(wallet, personId);
        return wallet;
    }

    public Wallet updateWallet(long id, Wallet wallet) {
        validator.validate(id);
        validator.validate(wallet);
        walletRepository.updateWallet(id, wallet);
        return wallet;
    }

    public void deleteWallet(long id) {
        validator.validate(id);
        walletRepository.deleteWallet(id);
    }

    public Wallet getWallet(long id) {
        validator.validate(id);
        Optional<Wallet> wallet = walletRepository.getWallet(id);
        if (wallet.isEmpty()) {
            throw new NotFoundException("Wallet not found");
        } else {
            return wallet.get();
        }
    }
}
