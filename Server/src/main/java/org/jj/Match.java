package org.jj;

import java.util.UUID;

public class Match {
    private final UUID agressorUuid;
    private final UUID passiveUuid;
    private final long amountTraded;
    private final long price;
    private final long timeStampMs;

    public Match(UUID agressorUuid, UUID passiveUuid, long amountTraded, long price, long timeStampMs) {
        this.agressorUuid = agressorUuid;
        this.passiveUuid = passiveUuid;
        this.amountTraded = amountTraded;
        this.price = price;
        this.timeStampMs = timeStampMs;
    }
}
