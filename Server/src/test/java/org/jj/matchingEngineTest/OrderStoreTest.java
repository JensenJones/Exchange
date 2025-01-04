package org.jj.matchingEngineTest;

import org.jj.matchingEngine.OrderStore;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStoreTest {
    OrderStore subject;

    @BeforeEach
    void setUp() {
        subject = new OrderStore();
    }

    @Test
    void shouldAddOrders() {
        subject.addOrder(1, 1);
        subject.addOrder(2, 1);
        subject.addOrder(3, 2);

        assertThat(subject.hasOrder(1)).isTrue();
        assertThat(subject.hasOrder(2)).isTrue();
        assertThat(subject.hasOrder(3)).isTrue();
    }

    @Test
    void shouldThrowExceptionForDuplicateOrder() {
        subject.addOrder(1, 1);
        subject.addOrder(2, 1);

        assertThatThrownBy(() -> subject.addOrder(2, 2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldGetOrder() {
        subject.addOrder(4, 8);

        assertThat(subject.getProductId(4)).isEqualTo(8);
    }

    @Test
    void shouldReturnNullForNonExistingOrder() {
        assertThat(subject.getProductId(12)).isNull();
    }
}