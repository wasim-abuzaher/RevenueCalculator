package com.wasimapps.revenuecalculator;

/**
 * Created by wasim on 2017-01-17.
 */

public class Product {
    private double mTotalPrice;

    public Product(double totalPrice) {
        mTotalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return mTotalPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Total Price=" + mTotalPrice +
                '}';
    }
}
