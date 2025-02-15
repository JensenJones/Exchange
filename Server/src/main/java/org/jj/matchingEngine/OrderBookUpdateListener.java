package org.jj.matchingEngine;

import io.grpc.stub.StreamObserver;
import org.jj.Service;

public interface OrderBookUpdateListener {
    void onOrderBookUpdate(Service.OrderBook orderBook);

    void removeResponseObserver(StreamObserver<Service.OrderBook> responseObserver);
}
