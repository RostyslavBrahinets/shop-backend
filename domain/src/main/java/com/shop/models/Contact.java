package com.shop.models;

import java.io.Serial;
import java.io.Serializable;

public class Contact implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private long id;
    private String email;
    private String phone;

    public Contact() {
    }

    public Contact(long id, String email, String phone) {
        this.id = id;
        this.email = email;
        this.phone = phone;
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
}
