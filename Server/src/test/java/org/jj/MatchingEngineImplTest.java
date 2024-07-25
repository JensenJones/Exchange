package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MatchingEngineImplTest {

    private MatchingEngineImpl subject;
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineImplTest.class);

    @BeforeEach
    void setup() {
        TimestampProvider timestampProvider = Mockito.mock(TimestampProvider.class);
        when(timestampProvider.getTimestamp()).thenReturn(10L);

        subject = new MatchingEngineImpl(timestampProvider, new IntIdProvider());
    }

    @Test
    void shouldCreateOrder() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY);

        assertThat(orderId).isEqualTo(1);
    }

    @Test
    void shouldCreateUniqueOrders() {
        int orderId1 = subject.createOrder(10, 10, BuySell.BUY);
        int orderId2 = subject.createOrder(10, 10, BuySell.BUY);
        int orderId3 = subject.createOrder(1, 1, BuySell.SELL);

        assertThat(orderId1).isEqualTo(1);
        assertThat(orderId2).isEqualTo(2);
        assertThat(orderId3).isEqualTo(3);
    }

    @Test
    void shouldCancelOrder() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY);

        assertThat(subject.cancelOrder(orderId)).isTrue();
    }

    @Test
    void shouldNotCancelNonExistentOrder() {
        assertThat(subject.cancelOrder(14)).isFalse();
    }

    @Test
    void shouldFillMatchingOrders() {
        int buyOrderId = subject.createOrder(10, 10, BuySell.BUY);
        int sellOrderId = subject.createOrder(10, 10, BuySell.SELL);

        assertThat(subject.cancelOrder(buyOrderId)).isFalse();
        assertThat(subject.cancelOrder(sellOrderId)).isFalse();

        int buyOrderId2 = subject.createOrder(10, 10, BuySell.BUY);
        int sellOrderId2 = subject.createOrder(5, 1, BuySell.SELL);

        assertThat(subject.cancelOrder(buyOrderId2)).isTrue();
        assertThat(subject.cancelOrder(sellOrderId2)).isFalse();
    }
}