package com.shop.controllers;

import com.shop.dto.RegistrationDto;
import com.shop.registration.RegistrationService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RegistrationController.REGISTRATION_URL)
public class RegistrationController {
    public static final String REGISTRATION_URL = "/web-api/registration";
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public String registration(
        @RequestBody RegistrationDto registrationDto
    ) throws StripeException {
        registrationService.registration(
            registrationDto.getFirstName(),
            registrationDto.getLastName(),
            registrationDto.getEmail(),
            registrationDto.getPhone(),
            registrationDto.getPassword(),
            registrationDto.getAdminNumber()
        );

        return "Successful Registration";
    }
}

