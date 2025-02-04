package org.jj;

import com.google.protobuf.Int32Value;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientProxy {
    private final OrderServiceGrpc.OrderServiceBlockingStub blockingStub;
    private final ManagedChannel channel;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProxy.class);

    public ClientProxy(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public int createOrder(int productId, int quantity, long price, BuySell buySell) {
        Service.OrderCreateRequestDetails request = Service.OrderCreateRequestDetails.newBuilder()
                                                           .setProductId(productId)
                                                           .setQuantity(quantity)
                                                           .setPrice(price)
                                                           .setBuySell(buySell == BuySell.BUY ? Service.BuySell.BUY : Service.BuySell.SELL)
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
}
