package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OrderBook {
    private final OrderBookSide buySide = new OrderBookBuySide(new SystemTimestampProvider());
    private final OrderBookSide sellSide = new OrderBookSellSide(new SystemTimestampProvider());

    public void addOrder(UUID uuid, BuySell buySell, long quantity, long price) {
        long quantityFilled = getOrderSide(BuySell.getOtherSide(buySell)).matchOrder(uuid, quantity, price);
        if (quantityFilled < quantity) {
            getOrderSide(buySell).addOrder(uuid, quantity, quantityFilled, price);
        }
    }

    public boolean cancelOrder(UUID uuid) {
        BuySell buySell = getOrderSide(uuid);
        if (buySell == null) {
            return false;
        }
        OrderBookSide sameSideOrders = getOrderSide(buySell);
        return sameSideOrders.removeOrder(uuid);
    }


    private OrderBookSide getOrderSide(BuySell buySell) {
        if (buySell == BuySell.BUY) {
            return buySide;
        }
        return sellSide;
    }

    @Nullable
    private BuySell getOrderSide(UUID uuid) {
        if (buySide.hasOrder(uuid)) {
            return BuySell.BUY;
        } else if (sellSide.hasOrder(uuid)) {
            return BuySell.SELL;
        } else {
            return null;
        }
    }
}
