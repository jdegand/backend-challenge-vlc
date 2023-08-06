package com.creditas;

import com.creditas.domain.Address;
import com.creditas.domain.CreditCard;
import com.creditas.domain.Customer;
import com.creditas.domain.Fulfillment;
import com.creditas.domain.Order;
import com.creditas.domain.ProductFactory;
import com.creditas.enums.ProductType;
import com.creditas.interfaces.Product;

public class Challenge {
    public static void main(String[] args) {
        Product shirt = ProductFactory.getProduct(ProductType.PHYSICAL, "Flowered t-shirt", 35.00);
        Product netflix = ProductFactory.getProduct(ProductType.MEMBERSHIP, "Familiar plan", 29.90);
        Product book = ProductFactory.getProduct(ProductType.BOOK, "The Hitchhiker's Guide to the Galaxy", 120.00);
        Product music = ProductFactory.getProduct(ProductType.DIGITAL, "Stairway to Heaven", 5.00);

        Order order = new Order(new Customer(), new Address());

        order.addProduct(shirt, 2);
        order.addProduct(netflix, 1);
        order.addProduct(book, 1);
        order.addProduct(music, 1);

        order.pay(new CreditCard("43567890-987654367"));

        Fulfillment fulfillment = new Fulfillment();
        fulfillment.setOrder(order);
        fulfillment.execute();
    }
}
