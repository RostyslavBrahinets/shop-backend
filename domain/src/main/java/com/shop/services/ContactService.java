package com.shop.services;

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

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact findById(long id) {
        contactValidator.validate(id);
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.orElseGet(Contact::new);
    }

    public Contact findByPerson(long personId) {
        personValidator.validate(personId);
        Optional<Contact> contact = contactRepository.findByPerson(personId);
        return contact.orElseGet(Contact::new);
    }

    public Contact save(
        String email,
        String phone,
        String password,
        long personId
    ) {
        contactValidator.validate(email, phone, password);
        personValidator.validate(personId);
        return contactRepository.save(email, phone, passwordEncoder.encode(password), personId);
    }

    public Contact update(long id, String phone) {
        contactValidator.validate(id);
        contactValidator.validatePhone(phone, id);
        return contactRepository.update(id, phone);
    }

    public void delete(long id) {
        contactValidator.validate(id);
        contactRepository.delete(id);
    }
}
