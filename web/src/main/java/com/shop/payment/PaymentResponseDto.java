package com.shop.payment;

public record PaymentResponseDto(
    String message,
    boolean isSuccessfully
) {
}

