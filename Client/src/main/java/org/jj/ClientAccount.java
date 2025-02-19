package org.jj;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientAccount {
    private UUID uuid;
    private Map<Integer, Order> idToOrderMap = new HashMap<>();
    private static final ClientAccount instance = new ClientAccount();

    private ClientAccount() {
        UUID.randomUUID();
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
}
