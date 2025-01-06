package org.jj.ClientTest;

import io.grpc.*;
import org.jj.client.ClientProxy;

import org.jj.matchingEngine.OrderStore;
import org.jj.orderService.OrderServiceServer;
import org.jj.product.BuySell;
import org.jj.product.ProductStore;
import org.jj.providers.IntIdProvider;
import org.jj.providers.MatchingEngineProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ClientProxyTest {
    private static final int PORT = 50051;
    private ManagedChannel channel;
    private OrderServiceServer server;
    private ClientProxy clientA;
    private int productId;

    @BeforeEach
    void setUp() throws IOException {
        ProductStore productStore = new ProductStore(new IntIdProvider());
        productId = productStore.addProduct("JENSEN JONES", "JJ");

        server = new OrderServiceServer(PORT, new MatchingEngineProvider(productStore), new OrderStore());
        server.start();
        channel = ManagedChannelBuilder.forAddress("localhost", PORT).usePlaintext().build();
        clientA = new ClientProxy(channel);

        assertThat(clientA).isNotNull();
        assertThat(channel.isShutdown()).isFalse();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        server.stop();
        server.blockUntilShutdown();
    }

    @Test
    void shouldShutdownClient() throws InterruptedException {
        clientA.shutdown();
        assertThat(channel.isShutdown()).isTrue();
    }

    @Test
    void shouldCreateOrder() {
        int orderAId = clientA.createOrder(productId, 10, 10, BuySell.BUY);
        assertThat(orderAId).isGreaterThanOrEqualTo(1);

        int orderBId = clientA.createOrder(productId, 10, 5, BuySell.SELL);
        assertThat(orderBId).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldFailToCreateIncorrectOrder() {
        int orderId = clientA.createOrder(-10, 10, 10, BuySell.BUY);
        assertThat(orderId).isEqualTo(-1);
    }

    @Test
    void shouldCancelOrder() {
        int orderId = clientA.createOrder(productId, 10, 10, BuySell.BUY);
        assertThat(orderId).isGreaterThanOrEqualTo(1);

        boolean cancelResult = clientA.cancelOrder(orderId);

        assertThat(cancelResult).isTrue();
    }

    @Test
    void shouldFillTradeBetweenTwoClients() {
        ClientProxy clientB = new ClientProxy(channel);

        int clientAOrderId = clientA.createOrder(productId, 10, 10, BuySell.BUY);
        int clientBOrderId = clientB.createOrder(productId, 10, 10, BuySell.SELL);

        boolean clientACancelRequest = clientA.cancelOrder(clientAOrderId);
        boolean clientBCancelRequest = clientB.cancelOrder(clientBOrderId);

        assertThat(clientACancelRequest).isFalse();
        assertThat(clientBCancelRequest).isFalse();
    }
}
