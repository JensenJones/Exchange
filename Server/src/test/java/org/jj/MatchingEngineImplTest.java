package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingEngineImplTest {

    private MatchingEngineImpl subject;



    @BeforeEach
    void setup() {
        subject = new MatchingEngineImpl();
    }

    @Test
    void shouldCreateOrder() {
        OrderState orderState = subject.createOrder(1, 10, BuySell.BUY);
        assertThat(orderState).isNotNull();
        assertThat(orderState.getOrderStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    void shouldCreateUniqueOrders() {
        OrderState order1 = subject.createOrder(1, 10, BuySell.BUY);
        OrderState order2 = subject.createOrder(1, 10, BuySell.BUY);
        OrderState order3 = subject.createOrder(1, 10, BuySell.BUY);

        assertThat(order1.getOrder().getId()).isNotIn(order2.getOrder().getId(), order3.getOrder().getId());
        assertThat(order2.getOrder().getId()).isNotIn(order3.getOrder().getId());
    }

    @Test
    void shouldNotFindNonExistentOrder() {
        OrderState lookedUpOrder = subject.getOrder(UUID.randomUUID());

        assertThat(lookedUpOrder).isNull();
    }

    @Test
    void shouldFindExistingOrder() {
        OrderState order = subject.createOrder(1, 10, BuySell.BUY);

        OrderState foundOrder = subject.getOrder(order.getOrder().getId());

        assert foundOrder != null;
        assertThat(foundOrder.getOrder().getId()).isEqualTo(order.getOrder().getId());
    }

    @Test
    void shouldCancelOrder() {
        OrderState order = subject.createOrder(1, 10, BuySell.BUY);

        order = subject.cancelOrder(order.getOrder().getId());

        assert order != null;
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void cancelNonExistentOrder() {
        OrderState order = subject.cancelOrder(UUID.randomUUID());
        assertThat(order).isNull();
    }
}