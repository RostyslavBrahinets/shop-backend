package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Component
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long id;
    private String firstName;
    private String lastName;
    private Contact contact;
    private Cart cart;
    private Wallet wallet;

    public Person() {
    }

    public Person(
        long id,
        String firstName,
        String lastName
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Person of(
        String firstName,
        String lastName
    ) {
        return new Person(0, firstName, lastName);
    }

    public Person withId(long id) {
        return new Person(id, this.firstName, this.lastName);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;
        return id == person.id
            && Objects.equals(firstName, person.firstName)
            && Objects.equals(lastName, person.lastName)
            && Objects.equals(contact, person.contact)
            && Objects.equals(cart, person.cart)
            && Objects.equals(wallet, person.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, contact, cart, wallet);
    }

    @Override
    public String toString() {
        return "Person{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + '}';
    }
}
