package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WalletValidator {
    private final WalletRepository walletRepository;

    public WalletValidator(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void validate(Wallet wallet) {
        String number = wallet.getNumber();
        double amountOfMoney = wallet.getAmountOfMoney();

        if (number == null || number.isBlank()) {
            throw new ValidationException("Number is invalid");
        } else if (amountOfMoney < 0) {
            throw new ValidationException("Amount of money is invalid");
        }
    }

    public void validate(long id) {
        List<Long> ids = new ArrayList<>();

        for (Wallet wallet : walletRepository.findAll()) {
            ids.add(wallet.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Wallet not found");
        }
    }
}