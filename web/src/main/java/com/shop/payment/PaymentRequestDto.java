package com.shop.payment;

public record PaymentRequestDto(
    int priceAmount,
    String cardNumber,
    String cardExpiry,
    String cardCvc
) {
}

