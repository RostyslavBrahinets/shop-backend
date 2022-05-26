package com.shop.repositories;

import com.shop.dao.ContactDao;
import com.shop.models.Contact;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ContactRepository {
    private final ContactDao contactDao;

    public ContactRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public List<Contact> findAll() {
        return contactDao.findAll();
    }

    public Optional<Contact> findById(long id) {
        return contactDao.findById(id);
    }

    public Optional<Contact> findByPerson(long personId) {
        return contactDao.findByPerson(personId);
    }

    public Contact save(Contact contact, long personId) {
        contactDao.save(
            contact.getEmail(),
            contact.getPhone(),
            contact.getPassword(),
            personId
        );
        return contact;
    }

    public void update(long id, Contact contact) {
        contactDao.update(id, contact.getPhone());
    }

    public void delete(long id) {
        contactDao.delete(id);
    }

    public int count() {
        return contactDao.findAll().size();
    }
}
