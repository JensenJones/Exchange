package org.jj;

public record Match(int aggressorOrderId, int passiveOrderId, long quantityTraded, double price, long timeStampMs) {}
