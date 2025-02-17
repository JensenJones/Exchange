package org.jj.matchingEngineTest;

import org.assertj.core.api.Fail;
import org.jj.Expiry;
import org.jj.matchingEngine.MatchingEngineImpl;
import org.jj.BuySell;
import org.jj.providers.IntIdProvider;
import org.jj.providers.TimestampProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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
    void shouldCreateGtcOrder() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);

        assertThat(orderId).isEqualTo(1);
    }

    @Test
    void shouldCreateFokOrder() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.FOK);
        assertThat(orderId).isEqualTo(1);
    }

    @Test
    void shouldCreateIocOrder() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);

        assertThat(orderId).isEqualTo(1);
    }

    @Test
    void shouldCreateGTC() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);

        assertThat(orderId).isEqualTo(1);
    }

    @Test
    void shouldCreateUniqueGtcOrders() {
        int orderId1 = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);
        int orderId2 = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);
        int orderId3 = subject.createOrder(1, 1, BuySell.SELL, Expiry.GTC);

        assertThat(orderId1).isEqualTo(1);
        assertThat(orderId2).isEqualTo(2);
        assertThat(orderId3).isEqualTo(3);
    }

    @Test
    void shouldCancelGTCOrder() {
        int orderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);

        assertThat(subject.cancelOrder(orderId)).isTrue();
    }

    @Test
    void shouldNotCancelNonExistentOrder() {
        assertThat(subject.cancelOrder(14)).isFalse();
    }

    @Test
    void shouldMatchOppositeGTCOrders() {
        int buyOrderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);
        int sellOrderId = subject.createOrder(10, 10, BuySell.SELL, Expiry.GTC);

        assertThat(subject.cancelOrder(buyOrderId)).isFalse();
        assertThat(subject.cancelOrder(sellOrderId)).isFalse();

        int buyOrderId2 = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);
        int sellOrderId2 = subject.createOrder(5, 1, BuySell.SELL, Expiry.GTC);

        assertThat(subject.cancelOrder(buyOrderId2)).isTrue();
        assertThat(subject.cancelOrder(sellOrderId2)).isFalse();
    }

    @Test
    void shouldFillFokOrder() {
        int passiveGTCBuyOrderId = subject.createOrder(10, 10, BuySell.BUY, Expiry.GTC);
        int fokSellOrderId = subject.createOrder(10, 10, BuySell.SELL, Expiry.FOK);

        assertThat(subject.cancelOrder(passiveGTCBuyOrderId)).isFalse();
        assertThat(subject.cancelOrder(fokSellOrderId)).isFalse();
    }

    @Test
    void shouldKillFokOrder() {
        int fokOrderOnBlankOrderBookId = subject.createOrder(100, 2, BuySell.BUY, Expiry.FOK);
        assertThat(subject.cancelOrder(fokOrderOnBlankOrderBookId)).isFalse();

        int passiveGTCBuyOrderId = subject.createOrder(2, 10, BuySell.BUY, Expiry.GTC);
        int fokPartialMatchOrderId = subject.createOrder(10, 10, BuySell.SELL, Expiry.FOK);

        assertThat(subject.cancelOrder(passiveGTCBuyOrderId)).isTrue();
        assertThat(subject.cancelOrder(fokPartialMatchOrderId)).isFalse();
    }

    @Test
    void shouldFillIocOrder() {
        int passiveBuyOrderId = subject.createOrder(100, 10, BuySell.BUY, Expiry.GTC);
        int aggressorSellIocOrder = subject.createOrder(100, 10, BuySell.SELL, Expiry.IOC);

        assertThat(subject.cancelOrder(passiveBuyOrderId)).isFalse();
        assertThat(subject.cancelOrder(aggressorSellIocOrder)).isFalse();
    }

    @Test
    void shouldPartialFillThenKillIocOrder() {
        int passiveBuyOrderId = subject.createOrder(50, 10, BuySell.BUY, Expiry.GTC);
        int aggressorSellIocOrder = subject.createOrder(100, 10, BuySell.SELL, Expiry.IOC);

        assertThat(subject.cancelOrder(passiveBuyOrderId)).isFalse();
        assertThat(subject.cancelOrder(aggressorSellIocOrder)).isFalse();
    }

    @Test
    void shouldKillIocOrder() {
        int aggressorSellIocOrder = subject.createOrder(100, 10, BuySell.SELL, Expiry.IOC);

        assertThat(subject.cancelOrder(aggressorSellIocOrder)).isFalse();
    }

}