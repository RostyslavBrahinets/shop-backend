package com.shop.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodCreateParams.CardDetails;
import com.stripe.param.PaymentMethodCreateParams.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
public class PaymentService {
    private final String stripeSecretKey;

    public PaymentService(
        @Value("${stripe.secret.key}") String stripeSecretKey
    ) {
        this.stripeSecretKey = stripeSecretKey;
    }

    private PaymentMethod createPaymentMethod(
        String cardNumber,
        String cardExpiry,
        String cardCvc
    ) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
            .setType(Type.CARD)
            .setCard(CardDetails.builder()
                .setNumber(cardNumber)
                .setExpMonth(Long.parseLong(cardExpiry.split("/")[0]))
                .setExpYear(Long.parseLong("20" + cardExpiry.split("/")[1]))
                .setCvc(cardCvc)
                .build())
            .build();

        return PaymentMethod.create(params);
    }
}
