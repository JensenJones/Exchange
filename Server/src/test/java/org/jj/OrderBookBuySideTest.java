package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

public class OrderBookBuySideTest {
    OrderBookSide subject;

    @BeforeEach
    void setUp() {
        subject = new OrderBookSide(new SystemTimestampProvider(),  (l1, l2) -> l2.compareTo(l1));
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

        assertThat(subject.matchOrder(1, 1, 1)).isEqualTo(1);
    }

    @Test
    void shouldBuyFromCheaperSell() {
        subject.addOrder(1, 1, 0, 100);

        assertThat(subject.matchOrder(1, 1, 1)).isEqualTo(1);
    }

    @Test
    void shouldMatchSamePriceDifferentQuantity() {
        subject.addOrder(1, 10, 0, 1);

        assertThat(subject.matchOrder(1, 1, 1)).isEqualTo(1);
        assertThat(subject.matchOrder(1, 1000, 1)).isEqualTo(9);
    }

    @Test
    void shouldMatchPartiallyFilledPassiveOrder() {
        subject.addOrder(1, 10, 5, 1);

        assertThat(subject.matchOrder(1, 6, 1)).isEqualTo(5);
    }

    @Test
    void shouldRemoveFilledPassiveOrder() {
        subject.addOrder(1, 1, 0, 1);
        subject.matchOrder(2, 1, 1);

        assertThat(subject.hasOrder(1)).isFalse();
    }
}
