package com.shop.adminnumber;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminNumberService {
    private final AdminNumberRepository adminNumberRepository;
    private final AdminNumberValidator adminNumberValidator;

    public AdminNumberService(
        AdminNumberRepository adminNumberRepository,
        AdminNumberValidator adminNumberValidator
    ) {
        this.adminNumberRepository = adminNumberRepository;
        this.adminNumberValidator = adminNumberValidator;
    }

    public List<AdminNumber> findAll() {
        return adminNumberRepository.findAll();
    }

    public AdminNumber findById(long id) {
        adminNumberValidator.validate(id, adminNumberRepository.findAll());
        Optional<AdminNumber> adminNumber = adminNumberRepository.findById(id);
        return adminNumber.orElseGet(AdminNumber::new);
    }

    public AdminNumber findByNumber(String number) {
        adminNumberValidator.validate(number, adminNumberRepository.findAll());
        Optional<AdminNumber> adminNumber = adminNumberRepository.findByNumber(number);
        return adminNumber.orElseGet(AdminNumber::new);
    }
}
