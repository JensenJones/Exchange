package org.jj;

import java.util.*;

public class OrderBookSellSide extends OrderBookSide{

    public OrderBookSellSide(TimestampProvider timestampProvider) {
        super(timestampProvider);
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
                quantityTraded += trade(uuid, quantity - quantityTraded, current);

                if (current.getQuantityRemaining() == 0) {
                    uuidToNodeMap.remove(current.getUuid());
                    ordersAtPrice.removeNode(current);
                    if (ordersAtPrice.gethead() == null) {
                        iterator.remove();
                    }
                }

                if (quantityTraded == quantity) {
                    break;
                }
                current = current.getNext();
            }
        }
        return quantityTraded;
    }
}
