package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MatchingEngineImpl implements MatchingEngine {
    private final Map<UUID, OrderState> allOrders = new HashMap<>();
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

        matchOrder(orderState);

        allOrders.put(uuid, orderState);

        return orderState;
    }

    private void matchOrder(OrderState orderState) {
        // TODO Can first check if another order on same side with same price exists and if so just add to the active buy orders.
        if (orderState.getOrder().getBuySell() == BuySell.BUY) {
            List<Double> prices = new ArrayList<>(activeSellOrders.keySet());
            Collections.sort(prices);

            for (double price : prices) {
                if (price < orderState.getOrder().getPrice()) {
                    for (OrderState otherSide : activeSellOrders.get(price)) {
                        if (trade(orderState, otherSide)) {
                            return;
                        }
                    }
                }
            }
            activeBuyOrders.computeIfAbsent(orderState.getOrder().getPrice(), k -> new LinkedList<>()).add(orderState);
        } else { // BuySell == BuySell.SELL
            List<Double> prices = new ArrayList<>(activeBuyOrders.keySet());
            Collections.sort(prices);
            Collections.reverse(prices);

            for (double price : prices) {
                if (price > orderState.getOrder().getPrice()) {
                    for (OrderState otherSide : activeBuyOrders.get(price)) {
                        if (trade(orderState, otherSide)) {
                            return;
                        }
                    }
                }
            }
            activeSellOrders.computeIfAbsent(orderState.getOrder().getPrice(), k -> new LinkedList<>()).add(orderState);
        }
    }

    private boolean trade(OrderState orderState, OrderState otherSide) {
        // Returns true if orderState is fulfilled
        double tradeQuantity = Math.min(orderState.getOrder().getQuantity() - orderState.getOrder().getQuantityFilled(),
                                        otherSide.getOrder().getQuantity() - otherSide.getOrder().getQuantityFilled());
        
        // Need to send money to people involved somehow

        // I THINK HERE IS WHERE TRADE DOESN'T GET SAVED
        orderState = updateOrderState(orderState, tradeQuantity);
        otherSide = updateOrderState(otherSide, tradeQuantity);

        return orderState.getOrderStatus() == OrderStatus.Filled;
    }

    private static OrderState updateOrderState(OrderState orderState, double tradeQuantity) {
        Order updatedOrder = orderState.getOrder().fillOrder(tradeQuantity);
        OrderStatus updatedOrderStatus;
        if (updatedOrder.getQuantityFilled() == updatedOrder.getQuantity()) {
            updatedOrderStatus = OrderStatus.Filled;
        } else {
            updatedOrderStatus = OrderStatus.PARTIALLY_FILLED;
        }
        return new OrderState(updatedOrder, updatedOrderStatus);
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

        if (orderState == null ||
            orderState.getOrderStatus() == OrderStatus.REJECTED ||
            orderState.getOrderStatus() == OrderStatus.CANCELED ||
            orderState.getOrderStatus() == OrderStatus.Filled) {
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
