package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Category;
import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ContactValidator {
    public void validate(
        String email,
        String phone,
        String password,
        List<Contact> contacts
    ) {
        if (isInValidEmail(email)) {
            throw new ValidationException("E-mail is invalid");
        } else if (isEmailAlreadyInUse(email, contacts)) {
            throw new ValidationException("E-mail is already in use");
        } else if (isInvalidPhone(phone)) {
            throw new ValidationException("Phone is invalid");
        } else if (isPhoneAlreadyInUse(phone, contacts)) {
            throw new ValidationException("Phone is already in use");
        } else if (password == null || password.isBlank()) {
            throw new ValidationException("Password is invalid");
        }
    }

    public void validate(long id, List<Contact> contacts) {
        List<Long> ids = new ArrayList<>();

        for (Contact contact : contacts) {
            ids.add(contact.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("Contact not found");
        }
    }

    public void validate(String email) {
        if (email == null || !email.equals("admin")) {
            if (isInValidEmail(email)) {
                throw new ValidationException("E-mail is invalid");
            }
        }
    }

    public void validatePhone(String phone, Contact contact, List<Contact> contacts) {
        String currentPhone = contact.getPhone();

        if (isInvalidPhone(contact.getPhone())) {
            throw new ValidationException("Phone is invalid");
        } else if (isPhoneAlreadyInUse(phone, contacts) && !phone.equals(currentPhone)) {
            throw new ValidationException("Phone is already in use");
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

    private boolean isEmailAlreadyInUse(String email, List<Contact> contacts) {
        List<String> emails = new ArrayList<>();
        for (Contact contact : contacts) {
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

    private boolean isPhoneAlreadyInUse(String phone, List<Contact> contacts) {
        List<String> phones = new ArrayList<>();
        for (Contact contact : contacts) {
            phones.add(contact.getPhone());
        }
        return phones.contains(phone);
    }
}