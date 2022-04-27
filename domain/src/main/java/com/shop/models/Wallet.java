package com.shop.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Wallet implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private long id;
    private long number;
    private int amountOfMoney;

    public Wallet() {
    }

    public Wallet(
        long id,
        long number,
        int amountOfMoney
    ) {
        this.id = id;
        this.number = number;
        this.amountOfMoney = amountOfMoney;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(int amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Wallet wallet = (Wallet) o;
        return id == wallet.id
            && number == wallet.number
            && amountOfMoney == wallet.amountOfMoney;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, amountOfMoney);
    }

    @Override
    public String toString() {
        return "Wallet{"
            + "id=" + id
            + ", number=" + number
            + ", amountOfMoney=" + amountOfMoney
            + '}';
    }
}
