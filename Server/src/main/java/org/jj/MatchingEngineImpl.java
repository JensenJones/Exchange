package org.jj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        int id = idProvider.getNewId();
        orderBook.addOrder(id, buySell, quantity, price);
        return id;
    }

    @Override
    public boolean cancelOrder(int id) {
        return orderBook.cancelOrder(id);
    }
}
