package org.jj.matchingEngine;

import io.grpc.stub.StreamObserver;
import org.jj.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBookUpdateListenerImpl implements OrderBookUpdateListener {
    private final Set<StreamObserver<Service.OrderBook>> responseObserverSet = ConcurrentHashMap.newKeySet();

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookUpdateListenerImpl.class);

    public OrderBookUpdateListenerImpl() {
    }

    public void addResponseObserver(StreamObserver<Service.OrderBook> responseObserver) {
        responseObserverSet.add(responseObserver);
    }

    @Override
    public void onOrderBookUpdate(Service.OrderBook orderBook) {
        for (StreamObserver<Service.OrderBook> responseObserver : responseObserverSet) {
            try {
                responseObserver.onNext(orderBook);
            } catch (io.grpc.StatusRuntimeException e) {
                LOGGER.warn("Client disconnected, removing observer: {}", e.getStatus());
                responseObserverSet.remove(responseObserver);
            } catch (Exception e) {
                LOGGER.error("Unexpected error in order book streaming", e);
                responseObserverSet.remove(responseObserver);
                responseObserver.onError(e);
            }

        }
    }

    @Override
    public void removeResponseObserver(StreamObserver<Service.OrderBook> responseObserver) {
        responseObserverSet.remove(responseObserver);
        LOGGER.info("Removed response listener");
    }
}
