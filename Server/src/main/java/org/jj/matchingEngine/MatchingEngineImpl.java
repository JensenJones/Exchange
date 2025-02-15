package org.jj.matchingEngine;

import io.grpc.stub.StreamObserver;
import org.jj.BuySell;
import org.jj.Service;
import org.jj.providers.IdProvider;
import org.jj.providers.TimestampProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MatchingEngineImpl implements MatchingEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineImpl.class);

    private final OrderBook orderBook = new OrderBook(new OrderBookUpdateListenerImpl());

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

    public void addOrderBookListener(StreamObserver<Service.OrderBook> responseObserver) {
        orderBook.getListener().addResponseObserver(responseObserver);
    }

    public void removeOrderBookListener(StreamObserver<Service.OrderBook> responseObserver) {
        orderBook.getListener().removeResponseObserver(responseObserver);
    }
}
