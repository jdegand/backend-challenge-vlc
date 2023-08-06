package com.creditas.domain;

import java.util.Date;
import java.util.Objects;

import com.creditas.interfaces.PaymentMethod;

public class Payment {
    private Order order;
    private PaymentMethod paymentMethod;
    private Date paidAt;
    private long authorizationNumber;
    private double amount;
    private Invoice invoice;

    public Payment(Order order, PaymentMethod paymentMethod) {
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.paidAt = new Date();
        this.authorizationNumber = paidAt.getTime();
        this.amount = order.totalAmount();
        this.invoice = new Invoice(order);
    }

    public Order order() {
        return order;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Date paidAt() {
        return paidAt;
    }

    public long authorizationNumber() {
        return authorizationNumber;
    }

    public double amount() {
        return amount;
    }

    public Invoice invoice() {
        return invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Payment payment = (Payment) o;
        return authorizationNumber == payment.authorizationNumber &&
                Double.compare(payment.amount, amount) == 0 &&
                Objects.equals(order, payment.order) &&
                Objects.equals(paymentMethod, payment.paymentMethod) &&
                Objects.equals(paidAt, payment.paidAt) &&
                Objects.equals(invoice, payment.invoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, paymentMethod, paidAt, authorizationNumber, amount, invoice);
    }
}
