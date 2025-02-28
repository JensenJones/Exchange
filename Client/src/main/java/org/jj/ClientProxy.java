package org.jj;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import org.jj.Subscribers.TopOfBookSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ClientProxy {
    private final ManagedChannel channel;
    private final OrderServiceGrpc.OrderServiceBlockingStub blockingStub;
    private final OrderServiceGrpc.OrderServiceStub asyncStub;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProxy.class);
    private StreamObserver<Service.OrderBook> productOrderBookSubscriptionResponseObserver;

    public ClientProxy(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = OrderServiceGrpc.newBlockingStub(channel);
        this.asyncStub = OrderServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public int createOrder(String productSymbol, BuySell buySell, double price, long quantity, Expiry expiry) {
        Service.OrderCreateRequestDetails request = Service.OrderCreateRequestDetails.newBuilder()
                .setBuySell(buySell == BuySell.BUY ? Service.BuySell.BUY : Service.BuySell.SELL)
                .setProductSymbol(productSymbol)
                .setQuantity(quantity)
                .setPrice(price)
                .setExpiry(Service.Expiry.valueOf(expiry.toString()))
                .build();

        int response;

        try {
            response = blockingStub.orderCreateRequest(request).getValue();
            LOGGER.info("Order created with ID: {}", response);
        } catch (Exception e) {
            LOGGER.error("Failed to create order: ", e);
            response = -1;
        }

        return response;
    }

    public boolean cancelOrder(int orderId) {
        return blockingStub.orderCancelRequest(Int32Value.of(orderId)).getValue();
    }

    public List<String> getTradingProductsList() {
        return blockingStub.getTradingProductsRequest(Service.EmptyQuery.newBuilder().build()).getSymbolsList();
    }

    public void subscribeToProductOrderBook(String symbol, TopOfBookSubscriber listener) {

        StreamObserver<Service.OrderBook> responseObserver = getResponseObserver(listener);
        this.productOrderBookSubscriptionResponseObserver = responseObserver;

        asyncStub.orderBookSubscriptionService(StringValue.of(symbol), responseObserver);
    }

    public void unsubscribeFromProduct() {
        productOrderBookSubscriptionResponseObserver.onCompleted();
    }

    private static @NotNull StreamObserver<Service.OrderBook> getResponseObserver(TopOfBookSubscriber listener) {
        return new StreamObserver<Service.OrderBook>() {
            int messages = 0;

            @Override
            public void onNext(Service.OrderBook orderBook) {
                listener.onOrderBookUpdate(orderBook);
                messages++;
                LOGGER.info("Received order book update {}", messages);
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.error("Error receiving order book updates: {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                LOGGER.info("Order book stream closed by server.");
            }
        };
    }

    public List<Order> getOrders(final Set<Integer> orderIdList) {
        Service.OrderIdList request = Service.OrderIdList.newBuilder().addAllId(orderIdList).build();

        List<Order> response;

        try {
            response = blockingStub.getOrdersRequest(request).getOrdersList().stream().map(protoOrder -> {
                return new Order(protoOrder.getId(),
                        protoOrder.getProductSymbol(),
                        protoOrder.getPrice(),
                        protoOrder.getQuantity(),
                        protoOrder.getQuantityFilled(),
                        BuySell.valueOf(protoOrder.getBuySell().toString()));
            }).toList();
        } catch (Exception e) {
            response = new ArrayList<>();
            LOGGER.error("Failed to create order: ", e);
        }

        return response;
    }
}
