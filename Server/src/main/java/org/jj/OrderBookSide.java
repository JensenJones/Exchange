package org.jj;

import java.util.LinkedList;
import java.util.List;

public class OrderBookSide {
    private List<LinkedList<Order>> pricesOrders = new LinkedList<>();


    public OrderBookSide() {
    }

    public void addOrder(Order order) {
        long price = order.getPrice();
    }
}
