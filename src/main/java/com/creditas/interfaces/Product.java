package com.creditas.interfaces;

import com.creditas.domain.Order;

public interface Product {
    public abstract String name();
    public abstract double price();

    public abstract void fulfill(Order order);
}
