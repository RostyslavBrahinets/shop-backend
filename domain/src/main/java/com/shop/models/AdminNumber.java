package com.shop.models;

import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Component
public class AdminNumber implements Serializable {
    @Serial
    private static final long serialVersionUID = 9L;
    private long id;
    private String number;

    public AdminNumber() {
    }

    public AdminNumber(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public static AdminNumber of(String number) {
        return new AdminNumber(0, number);
    }

    public AdminNumber withId(long id) {
        return new AdminNumber(id, this.number);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdminNumber adminNumber = (AdminNumber) o;
        return id == adminNumber.id
            && Objects.equals(number, adminNumber.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "AdminNumber{"
            + "id=" + id
            + ", number='" + number + '\''
            + '}';
    }
}
