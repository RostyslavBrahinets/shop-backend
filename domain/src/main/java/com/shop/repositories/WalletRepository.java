package com.shop.repositories;

import com.shop.dao.WalletDao;
import com.shop.models.Wallet;

import java.util.List;
import java.util.Optional;

public class WalletRepository {
    private final WalletDao walletDao;

    public WalletRepository(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public List<Wallet> getWallets() {
        return walletDao.getWallets();
    }

    public void addWallet(Wallet wallet, long personId) {
        walletDao.addWallet(wallet, personId);
    }

    public void updateWallet(long id, Wallet wallet) {
        walletDao.updateWallet(id, wallet);
    }

    public void deleteWallet(long id) {
        walletDao.deleteWallet(id);
    }

    public Optional<Wallet> getWallet(long id) {
        return walletDao.getWallet(id);
    }
}
