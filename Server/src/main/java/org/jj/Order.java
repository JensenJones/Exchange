package org.jj;

import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID uuid;
    private final BuySell buySell;
    private final OrderState orderState;
    private final long time;
    private final long price;
    private final long quantity;

    public Order(UUID uuid, BuySell buySell, OrderState orderState, long time, long quantity, long price) {
        this.uuid = uuid;
        this.buySell = buySell;
        this.orderState = orderState;
        this.time = time;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(uuid, order.uuid);
    }

    public UUID getUuid(){
        return uuid;
    }

    public long getTime() {
        return time;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getPrice() {
        return price;
    }

    public BuySell getBuySell() {
        return buySell;
    }
}
