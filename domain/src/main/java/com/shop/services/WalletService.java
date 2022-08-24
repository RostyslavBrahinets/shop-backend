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
    private final PersonService personService;
    private final PersonValidator personValidator;

    public WalletService(
        WalletRepository walletRepository,
        WalletValidator walletValidator,
        PersonService personService,
        PersonValidator personValidator
    ) {
        this.walletRepository = walletRepository;
        this.walletValidator = walletValidator;
        this.personService = personService;
        this.personValidator = personValidator;
    }

    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    public Wallet findById(long id) {
        walletValidator.validate(id, walletRepository.findAll());
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.orElseGet(Wallet::new);
    }

    public Wallet findByPerson(long personId) {
        personValidator.validate(personId, personService.findAll());
        Optional<Wallet> wallet = walletRepository.findByPerson(personId);
        return wallet.orElseGet(Wallet::new);
    }

    public Wallet save(
        String number,
        double amountOfMoney,
        long personId
    ) {
        walletValidator.validate(number, amountOfMoney);
        personValidator.validate(personId, personService.findAll());
        return walletRepository.save(number, amountOfMoney, personId);
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
