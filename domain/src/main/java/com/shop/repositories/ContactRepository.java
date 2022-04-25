package com.shop.repositories;

import com.shop.dao.ContactDao;
import com.shop.models.Contact;

import java.util.List;
import java.util.Optional;

public class ContactRepository {
    private final ContactDao contactDao;

    public ContactRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public List<Contact> getContacts() {
        return contactDao.getContacts();
    }

    public void addContact(Contact contact, int personId) {
        contactDao.addContact(contact, personId);
    }

    public void deleteContact(int id) {
        contactDao.deleteContact(id);
    }

    public Optional<Contact> getContact(int id) {
        return contactDao.getContact(id);
    }
}
