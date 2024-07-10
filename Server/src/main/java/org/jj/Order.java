package org.jj;

import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final LocalDateTime dateTime;
    private final double price;
    private final double quantity;
    private final double quantityFilled = 0;
    private final BuySell buySell;

    public Order(UUID id, LocalDateTime time, double price, double quantity, BuySell type) {
        this.id = id;
        this.dateTime = time;
        this.price = price;
        this.quantity = quantity;
        this.buySell = type;
    }

    public UUID getId(){
        return id;
    }
}
