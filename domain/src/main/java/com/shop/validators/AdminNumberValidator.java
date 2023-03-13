package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.AdminNumber;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminNumberValidator {
    public void validate(long id, List<AdminNumber> adminNumbers) {
        List<Long> ids = new ArrayList<>();

        for (AdminNumber adminNumber : adminNumbers) {
            ids.add(adminNumber.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("AdminNumber not found");
        }
    }

    public void validate(String number, List<AdminNumber> adminNumbers) {
        if (number == null || number.isBlank()) {
            throw new ValidationException("Number of admin is invalid");
        } else if (number.length() < 8) {
            throw new ValidationException("Number of admin is short");
        }

        List<String> numbers = new ArrayList<>();

        for (AdminNumber adminNumber : adminNumbers) {
            numbers.add(adminNumber.getNumber());
        }

        if (!numbers.contains(number)) {
            throw new NotFoundException("AdminNumber not found");
        }
    }

    public void validateAdminNumber(String number) {
        if (number == null || number.isBlank()) {
            throw new ValidationException("Number of AdminNumber is invalid");
        }
    }
}