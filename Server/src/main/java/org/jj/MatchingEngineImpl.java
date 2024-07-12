package org.jj;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MatchingEngineImpl implements MatchingEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineImpl.class);

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

    private void matchOrder(OrderState newOrderState) {
        // TODO Can first check if another order on same side with same price exists and if so just add to the active buy orders.
        if (newOrderState.getOrder().getBuySell() == BuySell.BUY) {
            List<Double> prices = new ArrayList<>(activeSellOrders.keySet());
            Collections.sort(prices);

            for (double price : prices) {
                if (price <= newOrderState.getOrder().getPrice()) {
                    for (OrderState otherSide : activeSellOrders.get(price)) {
                        if (trade(newOrderState, otherSide)) {
                            return;
                        }
                    }
                }
            }
            System.out.println("DEBUG SELL ORDER ADDED TO activeSellOrders");
            activeBuyOrders.computeIfAbsent(newOrderState.getOrder().getPrice(), k -> new LinkedList<>()).add(newOrderState);
        } else { // BuySell == BuySell.SELL
            List<Double> prices = new ArrayList<>(activeBuyOrders.keySet());
            Collections.sort(prices);
            Collections.reverse(prices);

            for (double price : prices) {
                if (price >= newOrderState.getOrder().getPrice()) {
                    for (OrderState otherSide : activeBuyOrders.get(price)) {
                        if (trade(newOrderState, otherSide)) {
                            return;
                        }
                    }
                } else {
                    break;
                }
            }
            System.out.println("DEBUG SELL ORDER ADDED TO activeSellOrders");
            activeSellOrders.computeIfAbsent(newOrderState.getOrder().getPrice(), k -> new LinkedList<>()).add(newOrderState);
        }
    }

    private boolean trade(OrderState newOrderState, OrderState otherSide) {
        // Returns true if newOrderState is fulfilled
        double tradeQuantity = Math.min(newOrderState.getOrder().getQuantity() - newOrderState.getOrder().getQuantityFilled(),
                                            otherSide.getOrder().getQuantity() - otherSide.getOrder().getQuantityFilled());
        System.out.println("DEBUG trade quantity = " + tradeQuantity);

        // TODO Need to send money to people involved somehow

        OrderState updatedOrderState = updateOrderState(newOrderState, tradeQuantity);
        OrderState updatedOtherSide = updateOrderState(otherSide, tradeQuantity);

        double tradingPrice = newOrderState.getOrder().getPrice();
        if (otherSide.getOrder().getBuySell() == BuySell.SELL) {
            activeSellOrders.get(tradingPrice).set(activeSellOrders.get(tradingPrice).indexOf(otherSide), updatedOtherSide);
        } else {
            activeBuyOrders.get(tradingPrice).set(activeBuyOrders.get(tradingPrice).indexOf(otherSide), updatedOtherSide);
        }
        allOrders.put(otherSide.getOrder().getUuid(), updatedOtherSide);
        return updatedOrderState.getOrderStatus() == OrderStatus.FILLED;
    }

    private static OrderState updateOrderState(OrderState newOrderState, double tradeQuantity) {
        Order updatedOrder = newOrderState.getOrder().fillOrder(tradeQuantity);
        OrderStatus updatedOrderStatus;
        if (updatedOrder.getQuantityFilled() == updatedOrder.getQuantity()) {
            updatedOrderStatus = OrderStatus.FILLED;
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
            orderState.getOrderStatus() == OrderStatus.FILLED) {
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
