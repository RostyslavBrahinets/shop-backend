package com.shop.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(PaymentController.PAYMENT_URL)
public class PaymentController {
    public static final String PAYMENT_URL = "/api/payment";

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostMapping
    public ResponseEntity<String> createCharge(
        @RequestParam String token,
        @RequestParam BigDecimal amount,
        @RequestParam String currency
    ) {
        Stripe.apiKey = stripeSecretKey;

        ChargeCreateParams params =
            ChargeCreateParams.builder()
                .setAmount(amount.multiply(new BigDecimal(100)).longValueExact())
                .setCurrency(currency)
                .setDescription("Charge")
                .setSource(token)
                .build();

        try {
            Charge charge = Charge.create(params);
            return ResponseEntity.ok("Payment succeeded! Id: " + charge.getId());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
