package org.jj;

import java.util.*;

public class OrderBookBuySide extends OrderBookSide{

    public OrderBookBuySide() {
    }

    @Override
    public long matchOrder(UUID uuid, long quantity, long price) {
        long quantityTraded = 0;
        ListIterator<OrdersAtPrice> iterator = ordersByPrice.listIterator(ordersByPrice.size());

        while (iterator.hasPrevious()) {
            OrdersAtPrice ordersAtPrice = iterator.previous();
            if (ordersAtPrice.getPrice() < price) {
                break;
            }
            Node current = ordersAtPrice.gethead();
            while (current != null) {
                quantityTraded += trade(uuid, quantity, quantityTraded, current, ordersAtPrice, iterator);
                if (quantityTraded == quantity) {
                    break;
                }
                current = current.getNext();
            }
        }
        return quantityTraded;
    }
}
