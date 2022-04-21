package com.shop.models;

import java.io.Serial;
import java.io.Serializable;

public class Wallet implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private long id;
    private long number;
    private int amountOfMoney;

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
}
