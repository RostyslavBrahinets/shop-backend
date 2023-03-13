package com.shop.repositories;

import com.shop.dao.AdminNumberDao;
import com.shop.models.AdminNumber;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdminNumberRepository {
    private final AdminNumberDao adminNumberDao;

    public AdminNumberRepository(AdminNumberDao adminNumberDao) {
        this.adminNumberDao = adminNumberDao;
    }

    public Optional<AdminNumber> findByNumber(String number) {
        return adminNumberDao.findByNumber(number);
    }
}
