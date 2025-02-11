package org.jj;

public interface TopOfBookSubscriber {
    void startSubscription(String symbol);
    void onOrderBookUpdate(Service.OrderBook orderBook);
    void unsubscribe();
}

