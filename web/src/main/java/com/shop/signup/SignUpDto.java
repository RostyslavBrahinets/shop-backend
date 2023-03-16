package com.shop.signup;

public record SignUpDto(
    String firstName,
    String lastName,
    String email,
    String phone,
    String password,
    String adminNumber
) {
}
