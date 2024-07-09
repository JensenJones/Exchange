package org.jj;

public interface MatchingEngine {
    Order createOrder();
    Order lookupOrder(int id);
    Order cancelOrder(int id);
}
