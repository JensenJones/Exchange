package org.jj;

import java.util.*;

public class OrderBookSellSide extends OrderBookSide{

    public OrderBookSellSide() {
    }

    @Override
    public long matchOrder(UUID uuid, long quantity, long price) {
        long quantityTraded = 0;
        ListIterator<OrdersAtPrice> iterator = ordersByPrice.listIterator();

        while (iterator.hasNext()) {
            OrdersAtPrice ordersAtPrice = iterator.next();

            if (ordersAtPrice.getPrice() > price) {
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
