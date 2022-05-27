package com.shop.repositories;

import com.shop.dao.WalletDao;
import com.shop.models.Wallet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WalletRepository {
    private final WalletDao walletDao;

    public WalletRepository(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public List<Wallet> findAll() {
        return walletDao.findAll();
    }

    public Optional<Wallet> findById(long id) {
        return walletDao.findById(id);
    }

    public Optional<Wallet> findByPerson(long personId) {
        return walletDao.findByPerson(personId);
    }

    public Wallet save(
        String number,
        double amountOfMoney,
        long personId
    ) {
        walletDao.save(
            number,
            amountOfMoney,
            personId
        );

        return Wallet.of(number, amountOfMoney);
    }

    public Wallet update(long id, double amountOfMoney) {
        walletDao.update(id, amountOfMoney);
        return Wallet.of("", amountOfMoney).withId(id);
    }

    public void delete(long id) {
        walletDao.delete(id);
    }

    public int count() {
        return walletDao.findAll().size();
    }
}
