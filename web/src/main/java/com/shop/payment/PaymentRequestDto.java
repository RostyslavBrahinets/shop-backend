package com.shop.payment;

public record PaymentRequestDto(
    int amount,
    String cardNumber,
    String cardExpiry,
    String cardCvc
) {
}

