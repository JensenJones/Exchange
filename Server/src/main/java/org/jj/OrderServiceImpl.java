package org.jj;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;
import org.jj.MatchingEngine.MatchingEngineImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO - Write unit tests
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final MatchingEngineProvider matchingEngineProvider;

    private final OrderProductMap orderProductMap;

    public OrderServiceImpl(MatchingEngineProvider matchingEngineProvider, OrderProductMap orderProductMap) {
        this.matchingEngineProvider = matchingEngineProvider;
        this.orderProductMap = orderProductMap;
    }

    @Override
    public void orderCreateRequest(Service.OrderCreateRequestDetails request, StreamObserver<Int32Value> responseObserver) {
        int productId = request.getProductId();

        MatchingEngineImpl matchingEngine = matchingEngineProvider.getMatchingEngine(productId);

        if (matchingEngine == null) {
            responseObserver.onError(new IllegalArgumentException("Product does not exist"));
            return;
        }

        // TODO - having a BuySell property for client as a boolean is not nice.
        //        Work out how to let them use the enum? It may be a different buy sell enum
        BuySell buySell = request.getBuySell() ? BuySell.BUY : BuySell.SELL;

        int orderId = matchingEngine.createOrder(request.getQuantity(), request.getPrice(), buySell);

        orderProductMap.addOrder(orderId, productId);

        responseObserver.onNext(Int32Value.of(orderId));
        responseObserver.onCompleted();
    }

    @Override
    public void orderCancelRequest(Int32Value request, StreamObserver<BoolValue> responseObserver) {
        // TODO - Have a class
        int orderId = request.getValue();
        int productId = orderProductMap.getProductId(orderId);

        MatchingEngineImpl matchingEngine = matchingEngineProvider.getMatchingEngine(productId);

        if (matchingEngine == null) {
            responseObserver.onError(new IllegalArgumentException("Order ID does not exist"));
            return;
        }

        responseObserver.onNext(BoolValue.of(matchingEngine.cancelOrder(orderId)));
        responseObserver.onCompleted();
    }
}
