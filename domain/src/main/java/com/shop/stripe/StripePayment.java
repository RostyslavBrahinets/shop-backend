package com.shop.stripe;

import com.shop.models.Contact;
import com.shop.models.Person;
import com.shop.services.ContactService;
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
    private final ContactService contactService;
    @Value("${stripe.apikey}")
    private String stripeKey;

    public StripePayment(ContactService contactService) {
        this.contactService = contactService;
    }

    public Optional<Customer> saveCustomer(Person person) throws StripeException {
        Stripe.apiKey = stripeKey;

        Contact contact = contactService.getContactByPerson(person.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("name", person.getFirstName() + " " + person.getLastName());
        params.put("email", contact.getEmail());

        return Optional.ofNullable(Customer.create(params));
    }

    public Optional<Customer> findByIdCustomer(String id) throws StripeException {
        Stripe.apiKey = stripeKey;
        return Optional.ofNullable(Customer.retrieve(id));
    }
}
