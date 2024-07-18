package org.jj;

public interface MatchingEngine {

    int createOrder(long quantity, long price, BuySell buySell);

    boolean cancelOrder(int id);
}
