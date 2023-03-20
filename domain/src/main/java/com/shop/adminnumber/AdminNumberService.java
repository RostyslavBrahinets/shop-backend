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

    public AdminNumber findByNumber(AdminNumber adminNumber) {
        adminNumberValidator.validate(adminNumber.getNumber(), adminNumberRepository.findAll());
        Optional<AdminNumber> adminNumberOptional = adminNumberRepository.findByNumber(adminNumber.getNumber());
        return adminNumberOptional.orElseGet(AdminNumber::new);
    }

    public AdminNumber save(AdminNumber adminNumber) {
        adminNumberValidator.validateAdminNumber(adminNumber.getNumber());
        adminNumberRepository.save(adminNumber.getNumber());
        return AdminNumber.of(adminNumber.getNumber()).withId(adminNumberRepository.findAll().size() + 1);
    }

    public void delete(AdminNumber adminNumber) {
        adminNumberValidator.validate(adminNumber.getNumber(), adminNumberRepository.findAll());
        adminNumberRepository.delete(adminNumber.getNumber());
    }
}
