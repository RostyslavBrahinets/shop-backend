package com.shop.services;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Contact;
import com.shop.repositories.ContactRepository;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactValidator contactValidator;
    private final PersonValidator personValidator;
    private final PasswordEncoder passwordEncoder;

    public ContactService(
        ContactRepository contactRepository,
        ContactValidator contactValidator,
        PersonValidator personValidator,
        PasswordEncoder passwordEncoder
    ) {
        this.contactRepository = contactRepository;
        this.contactValidator = contactValidator;
        this.personValidator = personValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Contact> getContacts() {
        return contactRepository.getContacts();
    }

    public Contact addContact(Contact contact, long personId) {
        contactValidator.validate(contact);
        contact.setPassword(passwordEncoder.encode(contact.getPassword()));
        contactRepository.addContact(contact, personId);
        return contact;
    }

    public void deleteContact(long id) {
        contactValidator.validate(id);
        contactRepository.deleteContact(id);
    }

    public Contact getContact(long id) {
        contactValidator.validate(id);
        Optional<Contact> contact = contactRepository.getContact(id);
        if (contact.isEmpty()) {
            throw new NotFoundException("Contact not found");
        } else {
            return contact.get();
        }
    }

    public Contact getContactByPerson(long personId) {
        personValidator.validate(personId);
        Optional<Contact> contact = contactRepository.getContactByPerson(personId);
        if (contact.isEmpty()) {
            throw new NotFoundException("Contact not found");
        } else {
            return contact.get();
        }
    }

    public Contact updateContact(long id, Contact updatedContact) {
        personValidator.validate(id);
        contactRepository.updateContact(id, updatedContact);
        return updatedContact;
    }
}
