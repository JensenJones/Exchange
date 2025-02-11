package org.jj;

import io.grpc.*;

import org.jj.matchingEngine.OrderStore;
import org.jj.orderService.OrderServiceServer;
import org.jj.product.ProductStore;
import org.jj.providers.IntIdProvider;
import org.jj.providers.MatchingEngineProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientProxyTest {
    private static final int PORT = 50051;
    private ManagedChannel channel;
    private OrderServiceServer server;
    private ClientProxy clientA;
    private int productId;
    private final String productSymbol = "JJ";
    Expiry expiry = Expiry.GTC;

    @BeforeEach
    void setUp() throws IOException {
        ProductStore productStore = new ProductStore(new IntIdProvider());
        productStore.addProduct("JENSEN JONES", "JJ");

        server = new OrderServiceServer(PORT, new MatchingEngineProvider(productStore), new OrderStore());
        server.start();
        channel = Grpc.newChannelBuilder(String.format("localhost:%d", PORT), InsecureChannelCredentials.create()).build();
        clientA = new ClientProxy(channel);

        assertThat(clientA).isNotNull();
        assertThat(channel.isShutdown()).isFalse();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        server.stop();
        server.blockUntilShutdown();
        clientA.shutdown();
    }

    @Test
    void shouldShutdownClient() throws InterruptedException {
        clientA.shutdown();
        assertThat(channel.isShutdown()).isTrue();
    }

    @Test
    void shouldCreateOrder() {
        int orderAId = clientA.createOrder(productSymbol, BuySell.BUY, 10, 10, expiry);
        assertThat(orderAId).isGreaterThanOrEqualTo(1);

        int orderBId = clientA.createOrder(productSymbol, BuySell.SELL, 5, 10, expiry);
        assertThat(orderBId).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldFailToCreateIncorrectOrder() {
        int orderId = clientA.createOrder("ABC", BuySell.BUY, 10, 10, expiry);
        assertThat(orderId).isEqualTo(-1);
    }

    @Test
    void shouldCancelOrder() {
        int orderId = clientA.createOrder(productSymbol, BuySell.BUY, 10, 10, expiry);
        assertThat(orderId).isGreaterThanOrEqualTo(1);

        boolean cancelResult = clientA.cancelOrder(orderId);

        assertThat(cancelResult).isTrue();
    }

    @Test
    void shouldFillTradeBetweenTwoClients() {
        ClientProxy clientB = new ClientProxy(channel);

        int clientAOrderId = clientA.createOrder(productSymbol, BuySell.BUY, 10, 10, expiry);
        int clientBOrderId = clientB.createOrder(productSymbol, BuySell.SELL, 10, 10, expiry);

        boolean clientACancelRequest = clientA.cancelOrder(clientAOrderId);
        boolean clientBCancelRequest = clientB.cancelOrder(clientBOrderId);

        assertThat(clientACancelRequest).isFalse();
        assertThat(clientBCancelRequest).isFalse();
    }
}
