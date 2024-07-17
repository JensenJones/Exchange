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
    public UUID createOrder(long quantity, long price, BuySell buySell) {
        UUID uuid = idProvider.getUUID();
        OrderState orderState = orderBook.addOrder(uuid, buySell, quantity, price);
        Order order = new Order(uuid, buySell, orderState, timestampProvider.getTimestamp(), quantity, price);
        return order.getUuid();
    }

    @Override
    public boolean cancelOrder(UUID id) {
        return orderBook.cancelOrder(id);
    }
}
