package com.creditas.domain;

import com.creditas.enums.ProductType;
import com.creditas.interfaces.Product;

public class ProductFactory {

    private ProductFactory() {

    }

    // allow fulfill method to be included ? - use a strategy and pass that ? 
    public static Product getProduct(ProductType productType, String name, Double price){
        switch(productType) {
            case PHYSICAL: 
                return new PhysicalProduct(name, price);
            case BOOK: 
                return new BookProduct(name, price);
            case DIGITAL: 
                return new DigitalProduct(name, price);
            case MEMBERSHIP:
                return new MembershipProduct(name, price);
            default:
                throw new RuntimeException("ProductType not found");
        }
    }
    
}
