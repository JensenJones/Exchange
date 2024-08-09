package org.jj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class OrderProductMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProductMap.class);
    private final Map<Integer, Integer> orderIdToProductId = new HashMap<>();

    public OrderProductMap() {
    }

    public void addOrder(int orderId, int productId) {
        if (orderIdToProductId.put(orderId, productId) != null) {
            LOGGER.error("Order already added to OrderProductMap");
        }
    }

    public int getProductId(int orderId) {
        return orderIdToProductId.get(orderId);
    }
}
