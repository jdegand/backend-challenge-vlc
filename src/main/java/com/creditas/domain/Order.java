package com.creditas.domain;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.creditas.interfaces.PaymentMethod;
import com.creditas.interfaces.Product;

public class Order {
    // missing orderId
    // @Id
    // private String id;
    private Customer customer;
    private Address address;
    private Date closedAt;
    private Payment payment;

    private List<OrderItem> items = new LinkedList<>();

    public Order(Customer customer, Address address) {
        this.customer = customer;
        this.address = address;
        this.closedAt = null;
        this.payment = null;
    }

    public Customer customer() {
        return customer;
    }

    public Address address() {
        return address;
    }

    public Date closedAt() {
        return closedAt;
    }

    public Payment payment() {
        return payment;
    }

    public List<OrderItem> items() {
        return Collections.unmodifiableList(items);
    }

    public double totalAmount() {
        return items
                .stream()
                .map(orderItem -> orderItem.total()) // issue here
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public void addProduct(Product product, int quantity) {
        // original used orderItem.product but orderItem.product is private 
        // - need to use getter
        boolean productAlreadyAdded = items.stream().anyMatch(orderItem -> orderItem.product() == product); // problem?
        if (productAlreadyAdded) {
            throw new RuntimeException("The product have already been added. Change the amount if you want more.");
        }

        items.add(new OrderItem(product, quantity));
    }

    // better to move pay method out of order class -> create an OrderService?
    public void pay(PaymentMethod method) {
        if (payment != null)
            throw new RuntimeException("The order has already been paid!");

        if (items.size() == 0)
            throw new RuntimeException("Empty order can not be paid!");

        payment = new Payment(this, method);

        close();
    }

    private void close() {
        closedAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return Objects.equals(customer, order.customer) &&
                Objects.equals(address, order.address) &&
                Objects.equals(closedAt, order.closedAt) &&
                Objects.equals(payment, order.payment) &&
                Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, address, closedAt, payment, items);
    }
}
