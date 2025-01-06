package org.jj.providerTest;

import org.jj.matchingEngine.MatchingEngineImpl;
import org.jj.providers.MatchingEngineProvider;
import org.jj.product.ProductStore;
import org.jj.providers.IntIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingEngineProviderTest {
    MatchingEngineProvider subject;

    @BeforeEach
    void setUp() {
        subject = new MatchingEngineProvider(new ProductStore(new IntIdProvider()));
    }

    @Test
    void shouldReturnNullForNonExistentProduct() {
        assertThat(subject.getMatchingEngine(5)).isNull();
    }

    @Test
    void shouldCreateNewMatchingEngine() {
        assertThat(subject).isNotNull();
    }

    @Test
    void shouldUseExistingMatchingEngine() {
        ProductStore productStore = new ProductStore(new IntIdProvider());
        int id = productStore.addProduct("JensenProduct", "JJ");
        subject = new MatchingEngineProvider(productStore);

        assertThat(subject.getMatchingEngine(id)).isInstanceOf(MatchingEngineImpl.class);
    }
}