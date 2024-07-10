package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchingEngineImpl implements MatchingEngine {

    private final Map<UUID, OrderState> orders = new HashMap<>();
    private final TimestampProvider timestampProvider = new TimestampProvider();
    private final IdProvider idProvider = new IdProvider();

    @Override
    public OrderState createOrder(double price, double quantity, BuySell buySell) {
        UUID uuid = idProvider.getUUID();
        Order order = new Order(uuid, timestampProvider.getTimestamp(), price, quantity, buySell);
        OrderState orderState = new OrderState(order, OrderStatus.NEW);
        orders.put(uuid, orderState);
        return orderState;
    }

    @Nullable
    @Override
    public OrderState getOrder(UUID id) {
        return orders.get(id);
    }

    @Nullable
    @Override
    public OrderState cancelOrder(UUID id) {
        OrderState orderState = orders.get(id);
        if (orderState == null) {
            return null;
        }
        orderState.ChangeOrderStatus(OrderStatus.CANCELED);
        return orderState;
    }
}
