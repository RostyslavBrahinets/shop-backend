package com.shop.signup;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(SignUpController.SIGN_UP_URL)
public class SignUpController {
    public static final String SIGN_UP_URL = "/api/v1/sign-up";
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("")
    public SignUpDto signUp(
        @RequestBody SignUpDto signUpDto
    ) {
        signUpService.signUp(
            signUpDto.firstName(),
            signUpDto.lastName(),
            signUpDto.email(),
            signUpDto.phone(),
            signUpDto.password().toCharArray(),
            signUpDto.adminNumber()
        );

        return signUpDto;
    }
}

