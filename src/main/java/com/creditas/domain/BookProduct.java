package com.creditas.domain;

import com.creditas.interfaces.Product;

public class BookProduct implements Product {

    private String name;
    private double price;

    public BookProduct(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public double price() {
        return this.price;
    }

    @Override
    public void fulfill(Order order) {
        // pass order so you can check database for orderId and verify product is tied to an order 
        System.out.println("Book Product fulfill method called");
    }

    @Override
    public String toString() {
        return "BookProduct [name=" + name + ", price=" + price + "]";
    }
    
}
