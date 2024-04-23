package com.shop.signup;

import static com.shop.adminnumber.AdminNumberParameter.getNumber;
import static com.shop.user.UserParameter.*;

public class SignUpParameter {
    public static SignUpDto getSignUpData() {
        return new SignUpDto(
            getFirstName(),
            getLastName(),
            getEmail(),
            getPhone(),
            String.valueOf(getPassword()),
            getNumber()
        );
    }
}
