package com.shop.mvc;

import com.shop.exceptions.ValidationException;
import com.shop.models.Person;
import com.shop.models.Wallet;
import com.shop.services.*;
import com.shop.stripe.StripePayment;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class RegistrationViewController {
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
    public String registration(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            GrantedAuthority role = userDetails.getAuthorities().stream().toList().get(0);
            String authority = role.getAuthority();
            boolean alreadyRegistered = authority.equals("ROLE_ADMIN")
                || authority.equals("ROLE_USER");

            if (alreadyRegistered) {
                return "redirect:/";
            }
        }

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("password") String password,
        @RequestParam("confirm-password") String confirmPassword
    ) throws StripeException {
        if (isValidData(firstName, lastName, email, phone, password, confirmPassword)) {
            savePerson(firstName, lastName);
            long personId = findPersonId();
            saveContactForPerson(email, phone, password, personId);
            saveRoleForPerson(personId);
            saveBasketForPerson(personId);
            saveWalletForPerson(personId);
        }
        return "redirect:/login";
    }

    private boolean isValidData(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        String confirmPassword
    ) throws ValidationException {
        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Password don't equals confirm password");
        }

        personValidator.validate(firstName, lastName);
        contactValidator.validate(email, phone, password);
        return true;
    }

    private void savePerson(String firstName, String lastName) {
        personService.save(firstName, lastName);
    }

    private long findPersonId() {
        List<Person> people = personService.findAll();
        return people.get(people.size() - 1).getId();
    }

    private void saveContactForPerson(String email, String phone, String password, long personId) {
        contactService.save(email, phone, password, personId);
    }

    private void saveRoleForPerson(long personId) {
        personRoleService.saveRoleForPerson(personId, 2);
    }

    private void saveBasketForPerson(long personId) {
        basketService.save(0, personId);
    }

    private void saveWalletForPerson(long personId) throws StripeException {
        Optional<Customer> customer = stripePayment
            .saveCustomer(personService.findById(personId));
        if (customer.isPresent()) {
            Wallet wallet = new Wallet();
            wallet.setNumber(customer.get().getId());
            wallet.setAmountOfMoney(customer.get().getBalance());
            walletService.save(wallet.getNumber(), wallet.getAmountOfMoney(), personId);
        }
    }
}
