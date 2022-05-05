package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;

import java.util.ArrayList;
import java.util.List;

public class ContactValidator {
    private final ContactRepository contactRepository;

    public ContactValidator(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void validate(Contact contact) {
        String email = contact.getEmail();
        String phone = contact.getPhone();
        String password = contact.getPassword();

        if (email == null || email.isBlank()) {
            throw new ValidationException("E-mail of contact is invalid");
        } else if (phone == null || phone.isBlank()) {
            throw new ValidationException("Phone of contact is invalid");
        } else if (password == null || password.isBlank()) {
            throw new ValidationException("Password of contact is invalid");
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
}