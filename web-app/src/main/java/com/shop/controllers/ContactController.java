package com.shop.controllers;

import com.shop.dto.ContactDto;
import com.shop.models.Contact;
import com.shop.services.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ContactController.CONTACTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ContactController {
    public static final String CONTACTS_URL = "/web-api/contacts";
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> findAllContact() {
        return contactService.findAll();
    }

    @GetMapping("/{id}")
    public Contact findByIdContact(@PathVariable int id) {
        return contactService.findById(id);
    }

    @PostMapping
    public Contact saveContact(
        @RequestBody Contact contact,
        @RequestBody int personId
    ) {
        return contactService.save(
            contact.getEmail(),
            contact.getPhone(),
            contact.getPassword(),
            personId
        );
    }

    @PostMapping("/{id}")
    public Contact updateContact(
        @PathVariable long id,
        @RequestBody ContactDto contact
    ) {
        Contact oldContact = contactService.findByPerson(id);
        Contact updatedContact = new Contact();
        updatedContact.setPhone(contact.getPhone());
        updatedContact.setEmail(oldContact.getEmail());
        updatedContact.setPassword(oldContact.getPassword());
        return contactService.update(id, updatedContact.getPhone());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable int id) {
        contactService.delete(id);
    }
}
