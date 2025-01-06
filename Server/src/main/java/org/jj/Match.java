package org.jj;

public record Match(int aggressorUuid, int passiveUuid, long amountTraded, long price, long timeStampMs) {}
