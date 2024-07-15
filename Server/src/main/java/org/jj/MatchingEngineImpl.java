package org.jj;

import org.jetbrains.annotations.Nullable;
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
    public Order createOrder(long price, long quantity, BuySell buySell) {
        Order order = new Order(idProvider.getUUID(), buySell, timestampProvider.getTimestamp(), price, quantity);
        orderBook.newOrder(order);
        return order;
    }

    @Nullable
    @Override
    public Order getOrder(UUID id) {
        return orderBook.getOrder(id);
    }

    @Nullable
    @Override
    public Order cancelOrder(UUID id) {
        return orderBook.cancelOrder(id);
    }
}
