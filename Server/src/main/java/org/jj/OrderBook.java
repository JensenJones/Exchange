package org.jj;

public class OrderBook {
    private OrderBookSide buySide = new OrderBookSide();
    private OrderBookSide sellSide = new OrderBookSide();

    public OrderBookSide getSameSideOrders(BuySell buySell) {
        if (buySell == BuySell.BUY) {
            return buySide;
        }
        return sellSide;
    }

    public OrderBookSide getOtherSideOrders(BuySell buySell) {
        if (buySell == BuySell.SELL) {
            return sellSide;
        }
        return buySide;
    }
}
