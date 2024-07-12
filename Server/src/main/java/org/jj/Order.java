package org.jj;

import java.util.UUID;

public class Order {
    private final UUID id;
    private final BuySell buySell;
    private final long time;
    private final double price;
    private final double quantity;
    private final double quantityFilled;

    public Order(UUID id, BuySell type, long time, double price, double quantity) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.buySell = type;
        this.quantityFilled = 0;
    }

    public Order(UUID id, BuySell buySell, long time, double price, double quantity, double quantityFilled) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.buySell = buySell;
        this.quantityFilled = quantityFilled;
    }

    public UUID getId(){
        return id;
    }

    public long getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getQuantityFilled() {
        return quantityFilled;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    public Order fillOrder(double quantityTrading) {
        if (quantityTrading <= quantity - quantityFilled) {
            return new Order(id, buySell, time, price, quantity, quantityFilled + quantityTrading);
        }
        throw new IllegalArgumentException("Trading quantity exceeds available quantity");
    }
}
