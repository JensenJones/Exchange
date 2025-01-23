package org.jj.matchingEngine;

import org.jetbrains.annotations.Nullable;
import org.jj.BuySell;
import org.jj.providers.SystemTimestampProvider;

public class OrderBook {
    private final OrderBookSide buySide = new OrderBookSide(new SystemTimestampProvider(), (l1, l2) -> l2.compareTo(l1));
    private final OrderBookSide sellSide = new OrderBookSide(new SystemTimestampProvider(),  (l1, l2) -> l1.compareTo(l2));

    public void addOrder(int id, BuySell buySell, long quantity, long price) {
        long quantityFilled = getOrderSide(BuySell.getOtherSide(buySell)).matchOrder(id, quantity, price);
        if (quantityFilled < quantity) {
            getOrderSide(buySell).addOrder(id, quantity, quantityFilled, price);
        }
    }

    public boolean cancelOrder(int id) {
        BuySell buySell = getOrderSide(id);
        if (buySell == null) {
            return false;
        }
        OrderBookSide sameSideOrders = getOrderSide(buySell);
        return sameSideOrders.removeOrder(id);
    }


    private OrderBookSide getOrderSide(BuySell buySell) {
        if (buySell == BuySell.BUY) {
            return buySide;
        }
        return sellSide;
    }

    @Nullable
    private BuySell getOrderSide(int id) {
        if (buySide.hasOrder(id)) {
            return BuySell.BUY;
        } else if (sellSide.hasOrder(id)) {
            return BuySell.SELL;
        } else {
            return null;
        }
    }
}
