package org.jj.matchingEngineTest;

import org.jj.matchingEngine.OrderBookSide;
import org.jj.providers.SystemTimestampProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderBookSellSideTest {
    OrderBookSide subject;

    @BeforeEach
    void setUp() {
        subject = new OrderBookSide(new SystemTimestampProvider(),  (l1, l2) -> l1.compareTo(l2));
    }

    @Test
    void shouldAddOrder() {
        subject.addOrder(1, 1, 0, 1);

        assertThat(subject.hasOrder(1)).isTrue();
    }

    @Test
    void shouldRemoveOrder() {
        subject.addOrder(1, 1, 0, 1);

        subject.removeOrder(1);

        assertThat(subject.hasOrder(1)).isFalse();
    }

    @Test
    void shouldMatchOrderWithSamePrice() {
        subject.addOrder(1, 1, 0, 1);

        assertThat(subject.matchOrder(2, 1, 1)).isEqualTo(1);
    }

    @Test
    void shouldSellToHigherBuy() {
        subject.addOrder(1, 1, 0, 1);

        assertThat(subject.matchOrder(2, 1, 100)).isEqualTo(1);
    }

    @Test
    void shouldMatchSamePriceDifferentQuantity() {
        subject.addOrder(1, 10, 0, 1);

        assertThat(subject.matchOrder(2, 1, 1)).isEqualTo(1);
        assertThat(subject.matchOrder(3, 1000, 1)).isEqualTo(9);
    }

    @Test
    void shouldMatchPartiallyFilledPassiveOrder() {
        subject.addOrder(1, 10, 5, 1);

        assertThat(subject.matchOrder(2, 6, 1)).isEqualTo(5);
    }

    @Test
    void shouldRemoveFilledPassiveOrder() {
        subject.addOrder(1, 1, 0, 1);
        subject.matchOrder(2, 1, 1);

        assertThat(subject.hasOrder(1)).isFalse();
    }
}
