package org.jj.MatchingEngine;

import org.jj.BuySell;
import org.jj.Providers.IdProvider;
import org.jj.OrderBook;
import org.jj.Providers.TimestampProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchingEngineImpl implements MatchingEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineImpl.class);

    private final OrderBook orderBook = new OrderBook();

    private final TimestampProvider timestampProvider;
    private final IdProvider idProvider;

    public MatchingEngineImpl(TimestampProvider timestampProvider, IdProvider idProvider) {
        this.timestampProvider = timestampProvider;
        this.idProvider = idProvider;
    }

    @Override
    public int createOrder(long quantity, long price, BuySell buySell) {
        int id = idProvider.generateId();
        orderBook.addOrder(id, buySell, quantity, price);
        return id;
    }

    @Override
    public boolean cancelOrder(int id) {
        return orderBook.cancelOrder(id);
    }
}
