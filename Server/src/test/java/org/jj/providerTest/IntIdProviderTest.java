package org.jj.providerTest;

import org.jj.providers.IdProvider;
import org.jj.providers.IntIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class IntIdProviderTest {
    private IdProvider idProvider;

    @BeforeEach
    void setUp() {
        idProvider = new IntIdProvider();
    }

    @Test
    void shouldProvideMultipleUniqueId() {
        int id1 = idProvider.generateId();
        int id2 = idProvider.generateId();
        int id3 = idProvider.generateId();

        assertThat(id1).isEqualTo(1);
        assertThat(id2).isEqualTo(2);
        assertThat(id3).isEqualTo(3);
    }
}
