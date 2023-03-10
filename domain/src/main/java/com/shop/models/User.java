package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Component
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private long adminNumberId;
    private Cart cart;
    private Wallet wallet;

    public User() {
    }

    public User(
        long id,
        String firstName,
        String lastName
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(
        long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        long adminNumberId
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.adminNumberId = adminNumberId;
    }

    public static User of(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        long adminNumberId
    ) {
        return new User(
            0,
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumberId
        );
    }

    public static User of(
        String firstName,
        String lastName
    ) {
        return new User(
            0,
            firstName,
            lastName
        );
    }

    public User withId(long id) {
        return new User(
            id,
            this.firstName,
            this.lastName,
            this.email,
            this.phone,
            this.password,
            this.adminNumberId
        );
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getAdminNumberId() {
        return adminNumberId;
    }

    public void setAdminNumberId(long adminNumberId) {
        this.adminNumberId = adminNumberId;
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

        User user = (User) o;
        return id == user.id
            && Objects.equals(firstName, user.firstName)
            && Objects.equals(lastName, user.lastName)
            && Objects.equals(email, user.email)
            && Objects.equals(phone, user.phone)
            && Objects.equals(password, user.password)
            && adminNumberId == user.adminNumberId
            && Objects.equals(cart, user.cart)
            && Objects.equals(wallet, user.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            email,
            phone,
            password,
            adminNumberId,
            cart,
            wallet
        );
    }

    @Override
    public String toString() {
        return "user{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", email='" + email + '\''
            + ", phone='" + phone + '\''
            + '}';
    }
}
