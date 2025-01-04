package org.jj.orderService;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;
import org.jj.product.BuySell;
import org.jj.OrderServiceGrpc;
import org.jj.matchingEngine.OrderStore;
import org.jj.Service;
import org.jj.matchingEngine.MatchingEngineImpl;
import org.jj.providers.MatchingEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        int productId = request.getProductId();
        BuySell buySell = request.getBuySell() == Service.BuySell.BUY ? BuySell.BUY : BuySell.SELL;

        MatchingEngineImpl matchingEngine = matchingEngineProvider.getMatchingEngine(productId);

        if (matchingEngine == null) {
            LOGGER.error("Product does not exist: Matching Engine == null");
            responseObserver.onError(new IllegalArgumentException("Product does not exist"));
            return;
        }

        int orderId = matchingEngine.createOrder(request.getQuantity(), request.getPrice(), buySell);

        orderStore.addOrder(orderId, productId);

        responseObserver.onNext(Int32Value.of(orderId));
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
}
