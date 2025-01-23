package org.jj;

public enum BuySell {
    BUY,
    SELL,
    ;

    public static BuySell getOtherSide(BuySell buySell) {
        return buySell == BUY ? SELL : BUY;
    }
}
