package com.shop.mvc;

import com.shop.models.Basket;
import com.shop.models.Contact;
import com.shop.models.Person;
import com.shop.models.Wallet;
import com.shop.services.*;
import com.shop.stripe.StripePayment;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RegistrationViewController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationViewController.class);
    private final PersonService personService;
    private final ContactService contactService;
    private final PersonRoleService personRoleService;
    private final BasketService basketService;
    private final WalletService walletService;
    private final PersonValidator personValidator;
    private final ContactValidator contactValidator;
    private final StripePayment stripePayment;

    public RegistrationViewController(
        PersonService personService,
        ContactService contactService,
        PersonRoleService personRoleService,
        BasketService basketService,
        WalletService walletService,
        PersonValidator personValidator,
        ContactValidator contactValidator,
        StripePayment stripePayment
    ) {
        this.personService = personService;
        this.contactService = contactService;
        this.personRoleService = personRoleService;
        this.basketService = basketService;
        this.walletService = walletService;
        this.personValidator = personValidator;
        this.contactValidator = contactValidator;
        this.stripePayment = stripePayment;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("password") String password
    ) {
        if (isValidData(firstName, lastName, email, phone, password)) {
            addPerson(firstName, lastName);
            long personId = getPersonId();
            addContactForPerson(email, phone, password, personId);
            addRoleForPerson(personId);
            addBasketForPerson(personId);
            addWalletForPerson(personId);
        }
        return "redirect:/login";
    }

    private boolean isValidData(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password
    ) {
        Person person = getPerson(firstName, lastName);
        personValidator.validate(person);
        Contact contact = getContact(email, phone, password);
        contactValidator.validate(contact);
        return true;
    }

    private void addPerson(String firstName, String lastName) {
        Person person = getPerson(firstName, lastName);
        personService.addPerson(person);
    }

    private Person getPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }

    private long getPersonId() {
        List<Person> people = personService.getPeople();
        return people.get(people.size() - 1).getId();
    }

    private void addContactForPerson(String email, String phone, String password, long personId) {
        Contact contact = getContact(email, phone, password);
        contactService.addContact(contact, personId);
    }

    private Contact getContact(String email, String phone, String password) {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setPassword(password);
        return contact;
    }

    private void addRoleForPerson(long personId) {
        personRoleService.addRoleForPerson(personId, 2);
    }

    private void addBasketForPerson(long personId) {
        Basket basket = new Basket();
        basket.setTotalCost(0);
        basketService.addBasket(basket, personId);
    }

    private void addWalletForPerson(long personId) {
        try {
            Customer customer = stripePayment.saveCustomer(personService.getPerson(personId));
            Wallet wallet = new Wallet();
            wallet.setNumber(customer.getId());
            wallet.setAmountOfMoney(customer.getBalance());
            walletService.addWallet(wallet, personId);
        } catch (StripeException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
