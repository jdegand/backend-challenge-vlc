package com.creditas.domain;

public class Fulfillment {

    // private String status;
    // could make a status property with enums values of 
    // ['Delivered', 'Pending', 'Out for Delivery', 'Refunded', 'Returned'] etc

    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void execute() {

        for (OrderItem item : order.items()) {
            item.product().fulfill(order);
        }
    }

}
