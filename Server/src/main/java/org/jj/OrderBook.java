package org.jj;

import java.util.UUID;

public class OrderBook {
    private final OrderBookSide buySide = new OrderBookSide();
    private final OrderBookSide sellSide = new OrderBookSide();

    private OrderBookSide getSameSideOrders(BuySell buySell) {
        if (buySell == BuySell.BUY) {
            return buySide;
        }
        return sellSide;
    }

    private BuySell getSameSideOrders(UUID uuid) {
        if (buySide.hasOrder(uuid)) {
            return BuySell.BUY;
        } else if (sellSide.hasOrder(uuid)) {
            return BuySell.SELL;
        } else {
            return null;
        }
    }

    private OrderBookSide getOtherSideOrders(BuySell buySell) {
        if (buySell == BuySell.SELL) {
            return sellSide;
        }
        return buySide;
    }

    public void newOrder(Order order) {
        if (!getOtherSideOrders(order.getBuySell()).matchOrder(order)) {
            getSameSideOrders(order.getBuySell()).addOrder(order);
        }
    }

    public Order getOrder(UUID uuid) {
        if (buySide.hasOrder(uuid)) {
            return buySide.getOrder(uuid);
        } else {
            return sellSide.getOrder(uuid);
        }
    }

    public Order cancelOrder(UUID uuid) {
        BuySell buySell = getSameSideOrders(uuid);
        if (buySell == null) {
            return null;
        }
        OrderBookSide sameSideOrders = getSameSideOrders(buySell);
        return sameSideOrders.removeOrder(uuid);
    }

}
