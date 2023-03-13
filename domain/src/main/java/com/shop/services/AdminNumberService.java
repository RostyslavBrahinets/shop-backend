package com.shop.services;

import com.shop.models.AdminNumber;
import com.shop.repositories.AdminNumberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminNumberService {
    private final AdminNumberRepository adminNumberRepository;

    public AdminNumberService(AdminNumberRepository adminNumberRepository) {
        this.adminNumberRepository = adminNumberRepository;
    }

    public List<AdminNumber> findAll() {
        return adminNumberRepository.findAll();
    }

    public AdminNumber findByNumber(String number) {
        Optional<AdminNumber> adminNumber = adminNumberRepository.findByNumber(number);
        return adminNumber.orElseGet(AdminNumber::new);
    }
}
