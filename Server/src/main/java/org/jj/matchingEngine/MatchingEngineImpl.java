package org.jj.matchingEngine;

import io.grpc.stub.StreamObserver;
import org.jj.BuySell;
import org.jj.Expiry;
import org.jj.Service;
import org.jj.providers.IdProvider;
import org.jj.providers.TimestampProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public int createOrder(long quantity, long price, BuySell buySell, Expiry expiry) {
        int id = idProvider.generateId();

        switch (expiry) {
            case GTC -> orderBook.addGtcOrder(id, buySell, quantity, price);
            case IOC -> orderBook.addIocOrder(id, buySell, quantity, price);
            case FOK -> orderBook.addFokOrder(id, buySell, quantity, price);
        }

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

    public Service.OrderBook getInitialOrderBookForSubscriber() {
        return orderBook.getOrderBook();
    }
}
