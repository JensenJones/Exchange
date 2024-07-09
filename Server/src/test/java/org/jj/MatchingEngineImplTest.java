package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingEngineImplTest {

    private MatchingEngineImpl subject;

    @BeforeEach
    void setup() {
        subject = new MatchingEngineImpl();
    }

    @Test
    void shouldCreateOrder() {
        Order order = subject.createOrder();

        assertThat(order.getId()).isEqualTo(1);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    void shouldCreateUniqueOrders() {
        Order order1 = subject.createOrder();
        Order order2 = subject.createOrder();
        Order order3 = subject.createOrder();

        assertThat(order1.getId()).isEqualTo(1);
        assertThat(order2.getId()).isEqualTo(2);
        assertThat(order3.getId()).isEqualTo(3);
    }

    @Test
    void shouldNotFindNonExistentOrder() {
        Order lookedUpOrder = subject.getOrder(1000);

        assertThat(lookedUpOrder).isNull();
    }

    @Test
    void shouldFindExistingOrder() {
        Order order = subject.createOrder();

        Order foundOrder = subject.getOrder(order.getId());

        assertThat(foundOrder.getId()).isEqualTo(order.getId());
    }

    @Test
    void shouldCancelOrder() {
        Order order = subject.createOrder();

        order = subject.cancelOrder(order.getId());

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void cancelNonExistentOrder() {
        Order order = subject.cancelOrder(123);
        assertThat(order).isNull();
    }
}