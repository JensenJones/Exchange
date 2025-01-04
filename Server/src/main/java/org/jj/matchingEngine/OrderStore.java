package org.jj.matchingEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class OrderStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStore.class);

    private final Map<Integer, Integer> orderIdToProductId = new HashMap<>();

    public OrderStore() {
    }

    public void addOrder(int orderId, int productId) {
        if (orderIdToProductId.put(orderId, productId) != null) {
            LOGGER.error("Order ID {} already exists", orderId);
            throw new IllegalStateException("Order ID already exists");
        }
    }

    public boolean hasOrder(int orderId) {
        return orderIdToProductId.containsKey(orderId);
    }

    public Integer getProductId(int orderId) {
        return orderIdToProductId.get(orderId);
    }
}
