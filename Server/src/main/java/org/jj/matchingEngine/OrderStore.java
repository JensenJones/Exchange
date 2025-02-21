package org.jj.matchingEngine;

import org.jj.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStore.class);

    private final Map<Integer, Integer> orderIdToProductId = new HashMap<>();
    private final Map<Integer, Order> orderIdToOrder = new HashMap<>();

    public OrderStore() {
    }

    public void addOrderIdToProduct(int orderId, int productId) {
        if (orderIdToProductId.put(orderId, productId) != null) {
            LOGGER.error("Order ID {} already exists in orderIdToProductId", orderId);
            throw new IllegalStateException("Order ID already exists");
        }
    }

    public void addOrder(Order order) {
        if (orderIdToOrder.put(order.orderId(), order) != null) {
            LOGGER.error("Order ID {} already exists in orderIDToOrder", order.orderId());
            throw new IllegalStateException("Order ID already exists");
        }
    }

    public boolean hasOrder(int orderId) {
        return orderIdToProductId.containsKey(orderId);
    }

    public Integer getProductId(int orderId) {
        return orderIdToProductId.get(orderId);
    }

    public List<Order> getClientOrders(List<Integer> orderIdList) {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            orderIdList.stream().forEach(id -> orders.add(orderIdToOrder.get(id)));
        } catch (NullPointerException NPE) {
            LOGGER.error("Null Pointer Exceptions. orderIdList contains invalid argument(s)");
        }

        return orders;
    }
}
