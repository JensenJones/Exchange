package org.jj;

import org.jetbrains.annotations.Nullable;

public class OrderBook {
    private final OrderBookSide buySide = new OrderBookBuySide(new SystemTimestampProvider());
    private final OrderBookSide sellSide = new OrderBookSellSide(new SystemTimestampProvider());

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
