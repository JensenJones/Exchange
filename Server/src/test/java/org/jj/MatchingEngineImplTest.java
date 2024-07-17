package org.jj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MatchingEngineImplTest {

    private MatchingEngineImpl subject;
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineImplTest.class);

    @BeforeEach
    void setup() {
        TimestampProvider timestampProvider = Mockito.mock(TimestampProvider.class);
        when(timestampProvider.getTimestamp()).thenReturn(10L);

        subject = new MatchingEngineImpl(timestampProvider, new IdProviderImplUuid());
    }

    @Test
    void shouldCreateOrder() {

    }

    @Test
    void shouldCreateUniqueOrders() {

    }

    @Test
    void shouldNotFindNonExistentOrder() {

    }

    @Test
    void shouldFindExistingOrder() {

    }

    @Test
    void shouldCancelOrder() {

    }

    @Test
    void shouldNotCancelNonExistentOrder() {
    }
}