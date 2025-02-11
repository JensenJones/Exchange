package org.jj.orderService;

public class ServerMain {
    public static void main(String[] args) {
        try {
            OrderServiceServer server = new OrderServiceServer(50051);
            server.start();
            server.blockUntilShutdown();
        } catch (Exception e) {
            System.err.println("Server failed: " + e.getMessage());
        }
    }
}
