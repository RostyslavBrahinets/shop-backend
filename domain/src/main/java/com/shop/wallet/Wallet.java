package com.shop.wallet;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Wallet implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private long id;
    private String number;
    private double amountOfMoney;

    public Wallet() {
    }

    public Wallet(
        long id,
        String number,
        double amountOfMoney
    ) {
        this.id = id;
        this.number = number;
        this.amountOfMoney = amountOfMoney;
    }

    public static Wallet of(
        String number,
        double amountOfMoney
    ) {
        return new Wallet(0, number, amountOfMoney);
    }

    public Wallet withId(long id) {
        return new Wallet(id, this.number, this.amountOfMoney);
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

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet that = (Wallet) o;
        return Objects.equals(id, that.id)
            && amountOfMoney == that.amountOfMoney
            && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, amountOfMoney);
    }

    @Override
    public String toString() {
        return "Wallet{"
            + "id=" + id
            + ", number='" + number + '\''
            + ", amountOfMoney=" + amountOfMoney
            + '}';
    }
}
