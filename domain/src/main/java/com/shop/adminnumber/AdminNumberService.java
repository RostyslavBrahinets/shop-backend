package com.shop.adminnumber;

import com.shop.interfaces.ServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminNumberService implements ServiceInterface<AdminNumber> {
    private final AdminNumberRepository adminNumberRepository;
    private final AdminNumberValidator adminNumberValidator;

    public AdminNumberService(
        AdminNumberRepository adminNumberRepository,
        AdminNumberValidator adminNumberValidator
    ) {
        this.adminNumberRepository = adminNumberRepository;
        this.adminNumberValidator = adminNumberValidator;
    }

    @Override
    public List<AdminNumber> findAll() {
        return adminNumberRepository.findAll();
    }

    @Override
    public AdminNumber findById(long id) {
        adminNumberValidator.validate(id, adminNumberRepository.findAll());
        Optional<AdminNumber> adminNumber = adminNumberRepository.findById(id);
        return adminNumber.orElseGet(AdminNumber::new);
    }

    @Override
    public AdminNumber save(AdminNumber adminNumber) {
        adminNumberValidator.validateAdminNumber(adminNumber.getNumber());
        adminNumberRepository.save(AdminNumber.of(adminNumber.getNumber()));
        return AdminNumber.of(adminNumber.getNumber()).withId(adminNumberRepository.findAll().size() + 1);
    }

    @Override
    public AdminNumber update(AdminNumber adminNumber) {
        return new AdminNumber();
    }

    @Override
    public void delete(AdminNumber adminNumber) {
        adminNumberValidator.validate(adminNumber.getNumber(), adminNumberRepository.findAll());
        adminNumberRepository.delete(AdminNumber.of(adminNumber.getNumber()));
    }

    public AdminNumber findByNumber(AdminNumber adminNumber) {
        adminNumberValidator.validate(adminNumber.getNumber(), adminNumberRepository.findAll());
        Optional<AdminNumber> adminNumberOptional = adminNumberRepository.findByNumber(adminNumber.getNumber());
        return adminNumberOptional.orElseGet(AdminNumber::new);
    }
}
