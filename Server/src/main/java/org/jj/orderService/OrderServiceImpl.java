package org.jj.orderService;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import org.jj.BuySell;
import org.jj.OrderServiceGrpc;
import org.jj.matchingEngine.OrderStore;
import org.jj.Service;
import org.jj.matchingEngine.MatchingEngineImpl;
import org.jj.providers.MatchingEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        BuySell buySell = request.getBuySell() == Service.BuySell.BUY ? BuySell.BUY : BuySell.SELL;
        int productId = matchingEngineProvider.productSymbolToId(productSymbol);

        if (matchingEngine == null) {
            LOGGER.error("Product does not exist: Matching Engine == null");
            responseObserver.onError(new IllegalArgumentException("Product does not exist"));
        } else {
            int orderId = matchingEngine.createOrder(request.getQuantity(), Math.round(request.getPrice() * 1000), buySell); // TODO USE THE EXPIRY TYPE TOO

            orderStore.addOrder(orderId, productId);

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

        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (Context.current().isCancelled()) {
                    LOGGER.info("Client cancelled order book streaming for {}", symbol);
                    break;  // Stop sending updates
                }
                Service.OrderBook updatedOrderBook = matchingEngine.getOrderBook();
                responseObserver.onNext(updatedOrderBook);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Order book streaming interrupted for symbol: {}", symbol);
        } catch (Exception e) {
            LOGGER.error("Error in order book streaming: {}", e.getMessage());
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
            LOGGER.info("Streaming completed for {}", symbol);
        }
    }

}