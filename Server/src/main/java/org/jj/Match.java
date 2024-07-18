package org.jj;

public record Match(int agressorUuid, int passiveUuid, long amountTraded, long price, long timeStampMs) {}
