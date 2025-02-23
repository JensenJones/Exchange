package org.jj.orderService;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.jj.*;
import org.jj.matchingEngine.OrderStore;
import org.jj.matchingEngine.MatchingEngineImpl;
import org.jj.providers.MatchingEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final MatchingEngineProvider matchingEngineProvider;
    private final OrderStore orderStore;

    public OrderServiceImpl(MatchingEngineProvider matchingEngineProvider, OrderStore orderStore) {
        this.matchingEngineProvider = matchingEngineProvider;
        this.orderStore = orderStore;
    }

    @Override
    public void orderCreateRequest(Service.OrderCreateRequestDetails request, StreamObserver<Int32Value> responseObserver) {
        String productSymbol = request.getProductSymbol();

        MatchingEngineImpl matchingEngine = matchingEngineProvider.getMatchingEngine(productSymbol);

        int productId = matchingEngineProvider.productSymbolToId(productSymbol);

        if (matchingEngine == null) {
            LOGGER.error("Product does not exist: Matching Engine == null");
            responseObserver.onError(new IllegalArgumentException("Product does not exist"));
        } else {
            long quantity = request.getQuantity();
            double price = request.getPrice();
            BuySell buySell = BuySell.valueOf(request.getBuySell().toString());
            Expiry expiry = Expiry.valueOf(request.getExpiry().toString());
            int orderId = matchingEngine.createOrder(quantity,
                                                     Math.round(price * 1000),
                                                     buySell,
                                                     expiry);
            Order order = new Order(orderId, productSymbol, price, quantity, 0, buySell); // TODO adjust quantity filled
            orderStore.addOrderIdToProduct(orderId, productId);
            orderStore.addOrder(order);

            responseObserver.onNext(Int32Value.of(orderId));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void orderCancelRequest(Int32Value request, StreamObserver<BoolValue> responseObserver) {
        int orderId = request.getValue();
        int productId = orderStore.getProductId(orderId);

        MatchingEngineImpl matchingEngine = matchingEngineProvider.getMatchingEngine(productId);

        if (matchingEngine == null) {
            responseObserver.onError(new IllegalArgumentException("Order ID does not exist"));
            LOGGER.error("Order ID does not exist: Matching Engine == null");
            return;
        }

        responseObserver.onNext(BoolValue.of(matchingEngine.cancelOrder(orderId)));
        responseObserver.onCompleted();
    }

    @Override
    public void getTradingProduct(Service.EmptyQuery request, StreamObserver<Service.TradingProductsList> responseObserver) {
        responseObserver.onNext(Service.TradingProductsList.newBuilder()
                                                           .addAllSymbols(matchingEngineProvider.getAllTradingProducts())
                                                           .build());
        responseObserver.onCompleted();
    }

    @Override
    public void orderBookSubscriptionService(StringValue request, StreamObserver<Service.OrderBook> responseObserver) {
        String symbol = request.getValue();
        MatchingEngineImpl matchingEngine = matchingEngineProvider.getMatchingEngine(symbol);

        if (matchingEngine == null) {
            LOGGER.error("Matching Engine is null for provided product with symbol: {}", symbol);
            responseObserver.onError(new RuntimeException("No matching engine found for symbol: " + symbol));
            return;
        }

        // Initial return of order book in current state then listen for changes
        responseObserver.onNext(matchingEngine.getInitialOrderBookForSubscriber());

        matchingEngine.addOrderBookListener(responseObserver);

        ServerCallStreamObserver<Service.OrderBook> serverObserver = (ServerCallStreamObserver<Service.OrderBook>) responseObserver;

        serverObserver.setOnCancelHandler(() -> {
            matchingEngine.removeOrderBookListener(responseObserver);
            LOGGER.info("Client disconnected, removed from observers: {}", symbol);
        });
    }

    @Override
    public void getOrders(Service.OrderIdList request, StreamObserver<Service.OrderList> responseObserver) {
        LOGGER.info("getOrders Request received. OrderIdList size = {}", request.getIdList().size());

        List<Order> clientOrders = orderStore.getClientOrders(request.getIdList());
        Service.OrderList response = Service.OrderList.newBuilder().addAllOrders(clientOrders.stream().map(Order::toProto).toList())
                                                                   .build();

        LOGGER.info("getOrder returning OrderList of size = {}", clientOrders.size());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}