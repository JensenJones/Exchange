package org.jj.matchingEngine;

import org.jj.BuySell;
import org.jj.Expiry;

public interface MatchingEngine {

    int createOrder(long quantity, long price, BuySell buySell, Expiry expiry);

    boolean cancelOrder(int id);
}
