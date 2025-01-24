package org.jj.orderService;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.jetbrains.annotations.VisibleForTesting;
import org.jj.matchingEngine.OrderStore;
import org.jj.providers.MatchingEngineProvider;
import org.jj.product.ProductStore;
import org.jj.providers.IntIdProvider;

import java.io.IOException;
import java.net.ServerSocket;

public class OrderServiceServer {

    private final int port;
    private final Server server;

    public OrderServiceServer(int port) {
        this.port = (port == 0) ? findFreePort() : port;
        this.server = ServerBuilder.forPort(this.port)
                .addService(new OrderServiceImpl(new MatchingEngineProvider(new ProductStore(new IntIdProvider()))
                , new OrderStore()))
                .build();
    }

    @VisibleForTesting
    public OrderServiceServer(int port, MatchingEngineProvider matchingEngineProvider, OrderStore orderStore) {
        this.port = port;
        this.server = ServerBuilder.forPort(this.port)
                .addService(new OrderServiceImpl(matchingEngineProvider, orderStore))
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            OrderServiceServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (Exception e) {
            throw new RuntimeException("Unable to find a free port", e);
        }
    }
}

