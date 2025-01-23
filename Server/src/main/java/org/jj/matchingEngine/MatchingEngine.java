package org.jj.matchingEngine;

import org.jj.BuySell;

public interface MatchingEngine {

    int createOrder(long quantity, long price, BuySell buySell);

    boolean cancelOrder(int id);
}
