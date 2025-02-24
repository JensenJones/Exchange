package org.jj;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ClientAccount {
    private UUID uuid;
    private Map<Integer, Order> idToOrderMap = new HashMap<>();
    private Set<Integer> activeOrderIdSet = new HashSet<>();
    private Map<String, Long> productQuantityOwned = new TreeMap<>();
    private static final ClientAccount instance = new ClientAccount();
    private static ClientProxy clientProxy;
    private static final int PORT = 50051;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAccount.class);

    public ClientAccount() {
        uuid = UUID.randomUUID();
        ManagedChannel channel = Grpc.newChannelBuilder(String.format("localhost:%d", PORT), InsecureChannelCredentials.create()).build();
        clientProxy = new ClientProxy(channel);
    }

    public static ClientAccount getInstance() {
        return instance;
    }

    public Map<Integer, Order> getIdToOrderMap() {
        return idToOrderMap;
    }

    public void addOrder(Order order) {
        this.idToOrderMap.put(order.orderId(), order);
    }

    public void removeOrder(int orderId) {
        this.idToOrderMap.remove(orderId);
    }

    public ClientProxy getClientProxy() {
        return clientProxy;
    }

    public List<Order> getOrders() {
        LOGGER.info("getOrders method called, active order count = {}", activeOrderIdSet.size());
        List<Order> orders = clientProxy.getOrders(activeOrderIdSet);
        orders.stream().forEach(order -> {
            idToOrderMap.put(order.orderId(), order);
            if (order.quantityFilled() == order.quantity()) {
                activeOrderIdSet.remove(order.orderId());
            }
            updateOwnedProducts(order.product(), order.quantityFilled());
        });
        return new ArrayList<>(idToOrderMap.values());
    }

    private void updateOwnedProducts(String product, long quantityFilled) {
        productQuantityOwned.compute(product, (key, value) -> (value == null) ? quantityFilled : quantityFilled + value);
    }

    public void addOrderId(int orderId) {
        activeOrderIdSet.add(orderId);
    }

    public Long getQuantityOwned(String product) {
        getOrders();
        return productQuantityOwned.get(product);
    }
}
