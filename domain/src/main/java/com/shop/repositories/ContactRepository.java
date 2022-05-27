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

    public Contact save(
        String email,
        String phone,
        String password,
        long personId
    ) {
        contactDao.save(
            email,
            phone,
            password,
            personId
        );
        return Contact.of(email, phone, password);
    }

    public Contact update(
        long id,
        String phone
    ) {
        contactDao.update(id, phone);
        return Contact.of("", phone, "").withId(id);
    }

    public void delete(long id) {
        contactDao.delete(id);
    }

    public int count() {
        return contactDao.findAll().size();
    }
}
