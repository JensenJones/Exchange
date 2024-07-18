package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderBookSellSideTest {
    OrderBookSide subject;

    @BeforeEach
    void setUp() {
        subject = new OrderBookSellSide(new SystemTimestampProvider());
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
