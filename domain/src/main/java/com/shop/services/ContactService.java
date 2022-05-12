package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import com.shop.validators.ContactValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactValidator validator;
    private final PasswordEncoder passwordEncoder;

    public ContactService(
        ContactRepository contactRepository,
        ContactValidator validator,
        PasswordEncoder passwordEncoder
    ) {
        this.contactRepository = contactRepository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Contact> getContacts() {
        return contactRepository.getContacts();
    }

    public Contact addContact(Contact contact, long personId) {
        validator.validate(contact);
        contact.setPassword(passwordEncoder.encode(contact.getPassword()));
        contactRepository.addContact(contact, personId);
        return contact;
    }

    public Contact updateContact(long id, Contact contact) {
        validator.validate(id);
        validator.validate(contact);
        contactRepository.updateContact(id, contact);
        return contact;
    }

    public void deleteContact(long id) {
        validator.validate(id);
        contactRepository.deleteContact(id);
    }

    public Contact getContact(long id) {
        validator.validate(id);
        Optional<Contact> contact = contactRepository.getContact(id);
        if (contact.isEmpty()) {
            throw new NotFoundException("Contact not found");
        } else {
            return contact.get();
        }
    }
}
