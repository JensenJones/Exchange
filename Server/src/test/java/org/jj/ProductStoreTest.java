package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductStoreTest {
    ProductStore subject;

    @BeforeEach
    void setUp() {
        subject = new ProductStore(new IntIdProvider());
    }

    @Test
    void shouldAddProduct() {
        int id = subject.addProduct("JENSEN CO", "JJJJ");

        assertThat(subject.hasProduct(id)).isTrue();
    }

    @Test
    void shouldAddMultipleProducts() {
        int id1 = subject.addProduct("JENSEN1 CO", "JJ");
        int id2 = subject.addProduct("JENSEN2 CO", "JJJJ");

        assertThat(subject.hasProduct(id1)).isTrue();
        assertThat(subject.hasProduct(id2)).isTrue();
    }

    @Test
    void shouldRemoveCorrectProduct() {
        int id1 = subject.addProduct("JENSEN1 CO", "JJ");
        int id2 = subject.addProduct("JENSEN2 CO", "JJJJ");

        assertThat(subject.removeProduct(id1)).isTrue();
        assertThat(subject.hasProduct(id1)).isFalse();
    }
}