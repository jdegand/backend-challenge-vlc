package com.creditas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.creditas.domain.Address;
import com.creditas.domain.Customer;
import com.creditas.domain.Order;
import com.creditas.domain.ProductFactory;
import com.creditas.enums.ProductType;

public class ChallengeTest {

    @Test
    public void testAddProduct() {
        Customer customer = new Customer();
        Address address = new Address();
        Order order = new Order(customer, address);

        order.addProduct(ProductFactory.getProduct(ProductType.PHYSICAL,"Product 1", 10.0), 1);
        order.addProduct(ProductFactory.getProduct(ProductType.PHYSICAL, "Product 2", 11.0), 1);
        order.addProduct(ProductFactory.getProduct(ProductType.PHYSICAL, "Product 3", 12.0), 1);

        assertEquals(order.items().size(), 3);
    }

    @Test
    public void testTotalAmount() {
        Customer customer = new Customer();
        Address address = new Address();
        Order order = new Order(customer, address);

        order.addProduct(ProductFactory.getProduct(ProductType.BOOK, "Product 1", 10.0), 2);
        order.addProduct(ProductFactory.getProduct(ProductType.BOOK, "Product 2", 15.0), 2);
        double total = order.totalAmount();

        assertEquals(total, 50.0, 0.0);
    }
}
