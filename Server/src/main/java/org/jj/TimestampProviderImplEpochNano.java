package org.jj;

import java.time.Instant;

public class TimestampProviderImplEpochNano implements TimestampProvider {

    @Override
    public long getTimestamp() {
        return Instant.now().getEpochSecond() + Instant.now().getNano();
    }
}
