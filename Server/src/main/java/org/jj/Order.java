package org.jj;

public class Order {
    private final int id;
    private final OrderStatus orderStatus;

    public Order(int id, OrderStatus orderStatus) {
        this.id = id;
        this.orderStatus = orderStatus;
    }

    public int getId(){
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order changeOrderStatus(OrderStatus orderStatus) {
        return new Order(id, orderStatus);
    }
}
