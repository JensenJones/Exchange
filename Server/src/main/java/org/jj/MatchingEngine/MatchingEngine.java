package org.jj.MatchingEngine;

import org.jj.BuySell;

public interface MatchingEngine {

    int createOrder(long quantity, long price, BuySell buySell);

    boolean cancelOrder(int id);
}
