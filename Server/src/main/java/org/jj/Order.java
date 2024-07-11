package org.jj;

import java.util.UUID;

public class Order {
    private final UUID id;
    private final long time;
    private final double price;
    private final double quantity;
    private final double quantityFilled = 0;
    private final BuySell buySell;

    public Order(UUID id, BuySell type, long time, double price, double quantity) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.buySell = type;
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
}
