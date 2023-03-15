package com.shop.signup;

import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SignUpController.SIGN_UP_URL)
public class SignUpController {
    public static final String SIGN_UP_URL = "/web-api/sign-up";
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping
    public String signUp(
        @RequestBody SignUpDto signUpDto
    ) throws StripeException {
        signUpService.signUp(
            signUpDto.getFirstName(),
            signUpDto.getLastName(),
            signUpDto.getEmail(),
            signUpDto.getPhone(),
            signUpDto.getPassword(),
            signUpDto.getAdminNumber()
        );

        return "Successful Sign Up";
    }
}

