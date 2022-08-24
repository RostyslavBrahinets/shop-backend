package com.shop.services;

import com.shop.models.Person;
import com.shop.models.Wallet;
import com.shop.stripe.StripePayment;
import com.shop.validators.RegistrationValidator;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
    private final RegistrationValidator registrationValidator;
    private final PersonService personService;
    private final ContactService contactService;
    private final PersonRoleService personRoleService;
    private final BasketService basketService;
    private final WalletService walletService;
    private final StripePayment stripePayment;

    public RegistrationService(
        RegistrationValidator registrationValidator,
        PersonService personService,
        ContactService contactService,
        PersonRoleService personRoleService,
        BasketService basketService,
        WalletService walletService,
        StripePayment stripePayment
    ) {
        this.registrationValidator = registrationValidator;
        this.personService = personService;
        this.contactService = contactService;
        this.personRoleService = personRoleService;
        this.basketService = basketService;
        this.walletService = walletService;
        this.stripePayment = stripePayment;
    }

    public void registration(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password
    ) throws StripeException {
        boolean validData = registrationValidator.isValidData(
            firstName,
            lastName,
            email,
            phone,
            password,
            contactService.findAll()
        );

        if (validData) {
            personService.save(firstName, lastName);
            long personId = findPersonId();
            contactService.save(email, phone, password, personId);
            personRoleService.saveRoleForPerson(personId, 2);
            basketService.save(0, personId);
            saveWalletForPerson(personId);
        }
    }

    private long findPersonId() {
        List<Person> people = personService.findAll();
        return people.get(people.size() - 1).getId();
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
