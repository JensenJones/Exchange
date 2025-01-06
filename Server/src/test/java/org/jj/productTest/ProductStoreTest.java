package org.jj.productTest;

import org.jj.product.ProductStore;
import org.jj.providers.IntIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductStoreTest {
    ProductStore subject;

    @BeforeEach
    void setUp() {
        subject = new ProductStore(new IntIdProvider());
    }

    @Test
    void shouldAddProduct() {
        int id = subject.addProduct("JENSEN CO", "JJJJ");

        assertThat(subject.containsProduct(id)).isTrue();
    }

    @Test
    void shouldAddMultipleProducts() {
        int id1 = subject.addProduct("JENSEN1 CO", "JJ");
        int id2 = subject.addProduct("JENSEN2 CO", "JJJJ");

        assertThat(subject.containsProduct(id1)).isTrue();
        assertThat(subject.containsProduct(id2)).isTrue();
    }

    @Test
    void shouldRemoveCorrectProduct() {
        int id1 = subject.addProduct("JENSEN1 CO", "JJ");
        int id2 = subject.addProduct("JENSEN2 CO", "JJJJ");

        assertThat(subject.removeProduct(id1)).isTrue();
        assertThat(subject.containsProduct(id1)).isFalse();
    }
}