package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Component
public class Contact implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private long id;
    private String email;
    private String phone;
    private String password;

    public Contact() {
    }

    public Contact(
        long id,
        String email,
        String phone,
        String password
    ) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact contact = (Contact) o;
        return id == contact.id
            && Objects.equals(email, contact.email)
            && Objects.equals(phone, contact.phone)
            && Objects.equals(password, contact.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, password);
    }

    @Override
    public String toString() {
        return "Contact{"
            + "id=" + id
            + ", email='" + email + '\''
            + ", phone='" + phone + '\''
            + ", password='" + password + '\''
            + '}';
    }
}
