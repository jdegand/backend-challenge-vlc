package com.creditas.domain;

import java.util.Objects;

import com.creditas.interfaces.PaymentMethod;

public class CreditCard implements PaymentMethod {
    private String number;

    public CreditCard(String number) {
        this.number = number;
    }

    public String number() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
