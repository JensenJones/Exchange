package org.jj;

public class TopOfBookSubscriberImpl implements TopOfBookSubscriber {

    private final ClientProxy clientProxy;
    private Service.OrderBook orderBook;
    private final OrderBookUpdateCallback updateCallback;

    public TopOfBookSubscriberImpl(ClientProxy clientProxy, String symbol, OrderBookUpdateCallback updateCallback) {
        this.clientProxy = clientProxy;
        this.updateCallback = updateCallback;
        startSubscription(symbol);
    }

    public void startSubscription(String symbol) {
        clientProxy.subscribeToProductOrderBook(symbol, this);
    }

    @Override
    public void onOrderBookUpdate(Service.OrderBook orderBook) {
        this.orderBook = orderBook;
        updateCallback.onUpdate(orderBook);
    }

    public void unsubscribe() {
        clientProxy.unsubscribeFromProduct();
    }

    @FunctionalInterface
    public interface OrderBookUpdateCallback {
        void onUpdate(Service.OrderBook orderBook);
    }

}

