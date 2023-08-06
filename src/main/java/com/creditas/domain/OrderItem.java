package com.creditas.domain;

import java.util.Objects;

import com.creditas.interfaces.Product;

public class OrderItem {
        private Product product;
        private int quantity;

        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product product() {
            return product;
        }

        public int quantity() {
            return quantity;
        }

        public double total() {
            return product.price() * quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            OrderItem orderItem = (OrderItem) o;
            return quantity == orderItem.quantity &&
                    Objects.equals(product, orderItem.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, quantity);
        }
    }
