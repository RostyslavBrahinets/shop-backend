package com.shop.mvc;

import com.shop.models.Basket;
import com.shop.models.Contact;
import com.shop.models.Person;
import com.shop.models.Wallet;
import com.shop.services.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class RegistrationViewController {
    private final PersonService personService;
    private final ContactService contactService;
    private final PersonRoleService personRoleService;
    private final BasketService basketService;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationViewController(
        PersonService personService,
        ContactService contactService,
        PersonRoleService personRoleService,
        BasketService basketService,
        WalletService walletService,
        PasswordEncoder passwordEncoder
    ) {
        this.personService = personService;
        this.contactService = contactService;
        this.personRoleService = personRoleService;
        this.basketService = basketService;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
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
        addPerson(firstName, lastName);
        long personId = getPersonId();
        addContactForPerson(email, phone, password, personId);
        addRoleForPerson(personId);
        addBasketForPerson(personId);
        addWalletForPerson(personId);
        return "redirect:/login";
    }

    private void addPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        personService.addPerson(person);
    }

    private long getPersonId() {
        List<Person> people = personService.getPeople();
        return people.get(people.size() - 1).getId();
    }

    private void addContactForPerson(String email, String phone, String password, long personId) {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setPassword(passwordEncoder.encode(password));
        contactService.addContact(contact, personId);
    }

    private void addRoleForPerson(long personId) {
        personRoleService.addRoleForPerson(personId, 1);
    }

    private void addBasketForPerson(long personId) {
        Basket basket = new Basket();
        basket.setTotalCost(0);
        basketService.addBasket(basket, personId);
    }

    private void addWalletForPerson(long personId) {
        Wallet wallet = new Wallet();
        wallet.setNumber(getNumberOfWallet());
        wallet.setAmountOfMoney(0);
        walletService.addWallet(wallet, personId);
    }

    private String getNumberOfWallet() {
        List<String> numbers = getWalletsId();
        String number;
        Random random = new Random();

        do {
            number = String.valueOf(random.nextLong(1000000000000000L, 9999999999999999L));
        } while (numbers.contains(number));

        return number;
    }

    private List<String> getWalletsId() {
        List<String> numbers = new ArrayList<>();

        for (Wallet wallet : walletService.getWallets()) {
            numbers.add(wallet.getNumber());
        }

        return numbers;
    }
}
