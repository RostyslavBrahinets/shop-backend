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

    public void addWallet(Wallet wallet, int personId) {
        walletDao.addWallet(wallet, personId);
    }

    public void updateWallet(int id, Wallet wallet) {
        walletDao.updateWallet(id, wallet);
    }

    public void deleteWallet(int id) {
        walletDao.deleteWallet(id);
    }

    public Optional<Wallet> getWallet(int id) {
        return walletDao.getWallet(id);
    }
}
