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
    IdProvider idProvider;

    @BeforeEach
    void setUp() {
        subject = new OrderBookBuySide(new SystemTimestampProvider());

        idProvider = Mockito.mock(IdProviderImplUuid.class);
        when(idProvider.getUUID()).thenReturn(UUID.randomUUID());
    }

    @Test
    void shouldAddOrder() {
        subject.addOrder(idProvider.getUUID(), 1, 0, 1);

        assertThat(subject.hasOrder(idProvider.getUUID())).isTrue();
    }

    @Test
    void shouldRemoveOrder() {
        subject.addOrder(idProvider.getUUID(), 1, 0, 1);

        subject.removeOrder(idProvider.getUUID());

        assertThat(subject.hasOrder(idProvider.getUUID())).isFalse();
    }

    @Test
    void shouldMatchOrderWithSamePrice() {
        subject.addOrder(idProvider.getUUID(), 1, 0, 1);

        assertThat(subject.matchOrder(UUID.randomUUID(), 1, 1)).isEqualTo(1);
    }

    @Test
    void shouldSellToHigherBuy() {
        subject.addOrder(idProvider.getUUID(), 1, 0, 100);

        assertThat(subject.matchOrder(UUID.randomUUID(), 1, 1)).isEqualTo(1);
    }

    @Test
    void shouldMatchSamePriceDifferentQuantity() {
        subject.addOrder(idProvider.getUUID(), 10, 0, 1);

        assertThat(subject.matchOrder(UUID.randomUUID(), 1, 1)).isEqualTo(1);
        assertThat(subject.matchOrder(UUID.randomUUID(), 1000, 1)).isEqualTo(9);
    }

    @Test
    void shouldMatchPartiallyFilledPassiveOrder() {
        subject.addOrder(idProvider.getUUID(), 10, 5, 1);

        assertThat(subject.matchOrder(UUID.randomUUID(), 6, 1)).isEqualTo(5);
    }
}
