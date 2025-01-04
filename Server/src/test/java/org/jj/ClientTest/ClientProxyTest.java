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
    private ClientProxy client;
    private int productId;

    @BeforeEach
    void setUp() throws IOException {
        ProductStore productStore = new ProductStore(new IntIdProvider());
        productId = productStore.addProduct("JENSEN JONES", "JJ");

        server = new OrderServiceServer(PORT, new MatchingEngineProvider(productStore), new OrderStore());
        server.start();
        channel = ManagedChannelBuilder.forAddress("localhost", PORT).usePlaintext().build();
        client = new ClientProxy(channel);

        assertThat(client).isNotNull();
        assertThat(channel.isShutdown()).isFalse();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        server.stop();
        server.blockUntilShutdown();
    }

    @Test
    void shouldShutdownClient() throws InterruptedException {
        client.shutdown();
        assertThat(channel.isShutdown()).isTrue();
    }

    @Test
    void shouldCreateOrder() {
        int orderId = client.createOrder(productId, 10, 10, BuySell.BUY);
        assertThat(orderId).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldFailToCreateIncorrectOrder() {
        fail("Not Implemented");
    }

    @Test
    void shouldCancelOrder() {
        fail("Not Implemented");
    }

    @Test
    void shouldFillOrderAsAggressor() {
        fail("Not Implemented");
    }

    @Test
    void shouldFillOrderAsPassive() {
        fail("Not Implemented");
    }
}
