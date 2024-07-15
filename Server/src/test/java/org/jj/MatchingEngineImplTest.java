package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MatchingEngineImplTest {

    private MatchingEngineImpl subject;
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineImplTest.class);

    @BeforeEach
    void setup() {
        TimestampProvider timestampProvider = Mockito.mock(TimestampProvider.class);
        when(timestampProvider.getTimestamp()).thenReturn(10L);

        subject = new MatchingEngineImpl(timestampProvider, new IdProviderImpl());
    }

    @Test
    void shouldCreateOrder() {
        Order order = subject.createOrder(10, 10, BuySell.SELL);

        assertThat(order.getQuantity()).isEqualTo(10);
        assertThat(order.getPrice()).isEqualTo(10);
        assertThat(order.getBuySell()).isEqualTo(BuySell.SELL);
        assertThat(order.getQuantityFilled()).isEqualTo(0);
    }

    @Test
    void shouldCreateUniqueOrders() {
        Order order1 = subject.createOrder(10, 10, BuySell.SELL);
        Order order2 = subject.createOrder(10, 10, BuySell.SELL);

        assertThat(order1.equals(order2)).isFalse();
    }

    @Test
    void shouldNotFindNonExistentOrder() {
        Order order = subject.getOrder(UUID.randomUUID());

        assertThat(order).isNull();
    }

    @Test
    void shouldFindExistingOrder() {
        Order order = subject.createOrder(10, 10, BuySell.BUY);
        Order returnedOrder = subject.getOrder(order.getUuid());

        assertThat(order.equals(returnedOrder)).isTrue();
    }

    @Test
    void shouldCancelOrder() {
        Order order = subject.createOrder(10, 10, BuySell.SELL);
        subject.cancelOrder(order.getUuid());

        assertThat(subject.getOrder(order.getUuid())).isNull();
    }

    @Test
    void shouldNotCancelNonExistentOrder() {
        assertThat(subject.cancelOrder(UUID.randomUUID())).isNull();
    }
}