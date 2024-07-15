package org.jj;

import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID uuid;
    private final BuySell buySell;
    private final long time;
    private final long price;
    private final long quantity;
    private long quantityFilled;

    public Order(UUID uuid, BuySell type, long time, long price, long quantity) {
        this.uuid = uuid;
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.buySell = type;
        this.quantityFilled = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return time == order.time && price == order.price && quantity == order.quantity &&
                       quantityFilled == order.quantityFilled && Objects.equals(uuid, order.uuid) && buySell == order.buySell;
    }

    public UUID getUuid(){
        return uuid;
    }

    public long getTime() {
        return time;
    }

    public long getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getQuantityFilled() {
        return quantityFilled;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    public void trade(Order order) {
        long tradingQuantity = Math.min(quantity - quantityFilled, order.getQuantity() - order.getQuantityFilled());
        quantityFilled += tradingQuantity;
        order.quantityFilled += tradingQuantity;
    }
}
