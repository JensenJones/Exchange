package org.jj;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMatchingEngineTest {

    @Test
    void createOrderHasPositiveID() {
        OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
        Order order = orderMatchingEngine.createOrder();
        assertThat(order.getID()).isGreaterThan(0);
    }

    @Test
    void createMultipleOrdersWithUniqueIDs() {
        OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
        Order order1 = orderMatchingEngine.createOrder();
        Order order2 = orderMatchingEngine.createOrder();
        Order order3 = orderMatchingEngine.createOrder();
        assertThat(order1.getID()).isNotIn(order2.getID(), order3.getID());
        assertThat(order2.getID()).isNotIn(order1.getID(), order3.getID());
    }

    @Test
    void createOrderHasActiveStatus() {
        OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
        Order order = orderMatchingEngine.createOrder();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACTIVE);
        System.out.println(order.getID());
    }

    @Test
    void lookupOrderFakeOrder() {
        OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
        Order lookedUpOrder = orderMatchingEngine.lookupOrder(1000);
        assertThat(lookedUpOrder).isEqualTo(null);
    }

    @Test
    void lookupOrderReturnsMatchingOrder() {
        OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
        Order order = orderMatchingEngine.createOrder();
        Order lookedUpOrder = orderMatchingEngine.lookupOrder(order.getID());
        assertThat(lookedUpOrder).isEqualTo(order);
    }

    @Test
    void cancelOrderHasCancelledStatus() {
        OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
        Order order = orderMatchingEngine.createOrder();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACTIVE);
        order = orderMatchingEngine.cancelOrder(order.getID());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }
}