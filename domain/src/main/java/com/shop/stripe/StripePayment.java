package com.shop.stripe;

import com.shop.user.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StripePayment {
    @Value("${stripe.apikey}")
    private String stripeKey;

    public StripePayment() {
    }

    public Optional<Customer> saveCustomer(User user) throws StripeException {
        Stripe.apiKey = stripeKey;

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstName() + " " + user.getLastName());
        params.put("email", user.getEmail());

        return Optional.ofNullable(Customer.create(params));
    }

    public Optional<Customer> findByIdCustomer(String id) throws StripeException {
        Stripe.apiKey = stripeKey;
        return Optional.ofNullable(Customer.retrieve(id));
    }

    public Optional<Customer> updateCustomer(
        String id,
        long newBalance
    ) throws StripeException {
        Stripe.apiKey = stripeKey;
        Customer customer = Customer.retrieve(id);
        Map<String, Object> params = new HashMap<>();
        params.put("balance", newBalance);
        return Optional.ofNullable(customer.update(params));
    }
}
