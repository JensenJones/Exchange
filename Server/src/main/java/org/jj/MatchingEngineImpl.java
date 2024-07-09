package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MatchingEngineImpl implements MatchingEngine {
    private final Map<Integer, Order> orders = new HashMap<>();
    private int lastId = 0;

    @Override
    public Order createOrder() {
        lastId++;
        Order order = new Order(lastId, OrderStatus.NEW);
        orders.put(lastId, order);
        return order;
    }

    @Nullable
    @Override
    public Order getOrder(int id) {
        return orders.get(id);
    }

    @Nullable
    @Override
    public Order cancelOrder(int id) {
        Order order = orders.get(id);
        if (order == null) {
            return null;
        }
        order = order.changeOrderStatus(OrderStatus.CANCELED);
        return order;
    }
}
