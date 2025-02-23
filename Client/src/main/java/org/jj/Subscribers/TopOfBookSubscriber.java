package org.jj.Subscribers;

import org.jj.Service;

public interface TopOfBookSubscriber {
    void startSubscription(String symbol);
    void onOrderBookUpdate(Service.OrderBook orderBook);
    void unsubscribe();
}

