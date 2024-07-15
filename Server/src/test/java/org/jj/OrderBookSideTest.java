package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

public class OrderBookSideTest {
    private OrderBookSide subject;

    @BeforeEach
    void setUp() {
        subject = new OrderBookSide();
    }

    @Test
    void shouldAddOrder() {
        Order order = new Order(UUID.randomUUID(), BuySell.BUY, 123L, 123L, 123L);
        subject.addOrder(order);
        Order orderReturned = subject.getOrder(order.getUuid());
        assertThat(order).isEqualTo(orderReturned);
    }

    @Test
    void shouldCancelOrder() {
        Order order = new Order(UUID.randomUUID(), BuySell.BUY, 123L, 123L, 123L);
        subject.addOrder(order);
        Order orderCancelled = subject.removeOrder(order.getUuid());
        assertThat(order).isEqualTo(orderCancelled);
        assertThat(subject.getOrder(order.getUuid())).isNull();
    }

    @Test
    void shouldMatchBuyToExistingSell() {
        Order sellOrder = new Order(UUID.randomUUID(), BuySell.SELL, 123, 1, 1);
        Order buyOrder = new Order(UUID.randomUUID(), BuySell.BUY, 123, 1, 1);

        subject.addOrder(sellOrder);
        subject.matchOrder(buyOrder);

        assertThat(subject.getOrder(sellOrder.getUuid())).isNull();
    }

    @Test
    void shouldMatchSellToExistingBuy() {
        Order buyOrder = new Order(UUID.randomUUID(), BuySell.BUY, 123, 1, 1);
        Order sellOrder = new Order(UUID.randomUUID(), BuySell.SELL, 123, 1, 1);

        subject.addOrder(buyOrder);
        subject.matchOrder(sellOrder);

        assertThat(subject.getOrder(sellOrder.getUuid())).isNull();
    }
}
