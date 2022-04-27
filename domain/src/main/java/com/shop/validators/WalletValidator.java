package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;

public class WalletValidator {
    private final WalletRepository walletRepository;

    public WalletValidator(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void validate(Wallet wallet) {
        String number = wallet.getNumber();
        double amountOfMoney = wallet.getAmountOfMoney();

        if (number == null || number.isBlank() || number.length() != 16) {
            throw new ValidationException("Name is invalid");
        } else if (amountOfMoney < 0) {
            throw new ValidationException("Describe is invalid");
        }
    }

    public void validate(int id) {
        if (id < 1 || id > walletRepository.getWallets().size()) {
            throw new NotFoundException("Product not found");
        }
    }
}