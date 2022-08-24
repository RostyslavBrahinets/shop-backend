package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Product;
import com.shop.models.Wallet;
import com.shop.repositories.WalletRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WalletValidator {
    public void validate(
        String number,
        double amountOfMoney
    ) {
        if (number == null || number.isBlank()) {
            throw new ValidationException("Number is invalid");
        } else if (amountOfMoney < 0) {
            throw new ValidationException("Amount of money is invalid");
        }
    }

    public void validateAmountOfMoney(double amountOfMoney) {
        if (amountOfMoney < 0) {
            throw new ValidationException("Amount of money is invalid");
        }
    }

    public void validate(long id, List<Wallet> wallets) {
        List<Long> ids = new ArrayList<>();

        for (Wallet wallet : wallets) {
            ids.add(wallet.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Wallet not found");
        }
    }
}