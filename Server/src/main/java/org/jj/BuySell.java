package org.jj;

public enum BuySell {
    BUY,
    SELL,
    ;

    static BuySell getOtherSide(BuySell buySell) {
        return buySell == BUY ? SELL : BUY;
    }
}
