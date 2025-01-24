package org.jj.orderService;

import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            OrderServiceServer server = new OrderServiceServer(serverSocket.getLocalPort());
            server.start();
            server.blockUntilShutdown();
        } catch (Exception e) {
            System.err.println("Server failed: " + e.getMessage());
        }
    }
}
