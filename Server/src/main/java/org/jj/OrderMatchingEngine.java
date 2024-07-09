package org.jj;

import java.util.HashMap;
import java.util.Map;

public class OrderMatchingEngine implements MatchingEngine {
    private final Map<Integer, Order> orders = new HashMap<>();
    private int lastID = 0;

    @Override
    public Order createOrder() {
        Order order = new Order(lastID + 1, OrderStatus.ACTIVE);
        orders.put(lastID + 1, order);
        lastID++;
        return order;
    }

    @Override
    public Order lookupOrder(int id) {
        return orders.get(id);
    }

    @Override
    public Order cancelOrder(int id) {
        Order order = orders.get(id);
        order = order.changeOrderStatus(OrderStatus.CANCELLED);
        return order;
    }
}
