package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContactValidator {
    private final ContactRepository contactRepository;

    public ContactValidator(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void validate(Contact contact) {
        String email = contact.getEmail();
        String phone = contact.getPhone();
        String password = contact.getPassword();

        if (isInValidEmail(email)) {
            throw new ValidationException("E-mail is invalid");
        } else if (isEmailAlreadyInUse(email)) {
            throw new ValidationException("E-mail is already in use");
        } else if (isInvalidPhone(phone)) {
            throw new ValidationException("Phone is invalid");
        } else if (isPhoneAlreadyInUse(phone)) {
            throw new ValidationException("Phone is already in use");
        } else if (password == null || password.isBlank()) {
            throw new ValidationException("Password is invalid");
        }
    }

    public void validate(long id) {
        List<Long> ids = new ArrayList<>();

        for (Contact contact : contactRepository.getContacts()) {
            ids.add(contact.getId());
        }

        if (id < 1 || !ids.contains(id)) {
            throw new NotFoundException("Contact not found");
        }
    }

    public void validate(String email) {
        if (isInValidEmail(email)) {
            throw new ValidationException("E-mail is invalid");
        }
    }

    private boolean isInValidEmail(String email) {
        return email == null
            || email.isBlank()
            || email.startsWith("@")
            || !email.contains("@")
            || !email.endsWith(".com")
            || email.endsWith("@.com");
    }

    private boolean isEmailAlreadyInUse(String email) {
        List<String> emails = new ArrayList<>();
        for (Contact contact : contactRepository.getContacts()) {
            emails.add(contact.getEmail());
        }
        return emails.contains(email);
    }

    private boolean isInvalidPhone(String phone) {
        return phone == null
            || phone.isBlank()
            || !phone.startsWith("+")
            || phone.length() < 12;
    }

    private boolean isPhoneAlreadyInUse(String phone) {
        List<String> phones = new ArrayList<>();
        for (Contact contact : contactRepository.getContacts()) {
            phones.add(contact.getPhone());
        }
        return phones.contains(phone);
    }
}