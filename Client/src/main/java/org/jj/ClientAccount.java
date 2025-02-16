package org.jj;

import java.util.HashMap;
import java.util.Map;

public class ClientAccount {
    private double balance = 0;
    private double depositedAmount = 0;
    private Map<Integer, Order> orderMap = new HashMap<>();

    private static final ClientAccount instance = new ClientAccount();

    private ClientAccount() {}

    public static ClientAccount getInstance() {
        return instance;
    }

    public double getBalance() {
        return balance;
    }

    public double getDepositedAmount() {
        return depositedAmount;
    }

    public Map<Integer, Order> getOrderMap() {
        return orderMap;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void addDepositedAmount(double depositedAmount) {
        this.depositedAmount += depositedAmount;
    }

    public void addOrder(Order order) {
        this.orderMap.put(order.orderId(), order);
    }

    public void removeOrder(int orderId) {
        this.orderMap.remove(orderId);
    }
}
