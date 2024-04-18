package com.shop.payment;

import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(PaymentController.PAYMENT_URL)
public class PaymentController {
    public static final String PAYMENT_URL = "/api/v1/payment";

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("")
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        try {
            String paymentInfo = paymentService.payment(
                paymentRequestDto.priceAmount(),
                paymentRequestDto.cardNumber(),
                paymentRequestDto.cardExpiry(),
                paymentRequestDto.cardCvc()
            );

            return new PaymentResponseDto(paymentInfo, true);
        } catch (StripeException e) {
            return new PaymentResponseDto(e.getMessage(), false);
        }
    }
}
