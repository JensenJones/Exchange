package org.jj;

public class OrderState {
    private final Order order;
    private OrderStatus orderStatus;

    public OrderState(Order order, OrderStatus orderStatus) {
        this.order = order;
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order getOrder() {
        return order;
    }
}
