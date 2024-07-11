package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MatchingEngineImpl implements MatchingEngine {
    private final Map<UUID, OrderState> allOrders = new HashMap<UUID, OrderState>();
    private final Map<Double, List<OrderState>> activeBuyOrders = new HashMap<>();
    private final Map<Double, List<OrderState>> activeSellOrders = new HashMap<>();

    TimestampProvider timestampProvider;
    private final IdProvider idProvider;

    public MatchingEngineImpl(TimestampProvider timestampProvider, IdProvider idProvider) {
        this.timestampProvider = timestampProvider;
        this.idProvider = idProvider;
    }

    @Override
    public OrderState createOrder(double price, double quantity, BuySell buySell) {
        UUID uuid = idProvider.getUUID();
        Order order = new Order(uuid, buySell, timestampProvider.getTimestamp(), price, quantity);
        OrderState orderState = new OrderState(order, OrderStatus.NEW);

        if (buySell == BuySell.BUY) {
            activeBuyOrders.computeIfAbsent(price, k -> new LinkedList<OrderState>()).add(orderState);
        } else {

            activeSellOrders.computeIfAbsent(price, k -> new LinkedList<OrderState>()).add(orderState);
        }

        allOrders.put(uuid, orderState);

        return orderState;
    }

    @Nullable
    @Override
    public OrderState getOrder(UUID id) {
        return allOrders.get(id);
    }

    @Nullable
    @Override
    public OrderState cancelOrder(UUID id) {
        OrderState orderState = allOrders.get(id);

        if (orderState == null) {
            return null;
        }

        BuySell buySell = orderState.getOrder().getBuySell();
        if (buySell == BuySell.BUY) {
            activeBuyOrders.get(orderState.getOrder().getPrice()).remove(orderState);
        } else {
            activeSellOrders.get(orderState.getOrder().getPrice()).remove(orderState);
        }

        orderState.ChangeOrderStatus(OrderStatus.CANCELED);
        return orderState;
    }
}
