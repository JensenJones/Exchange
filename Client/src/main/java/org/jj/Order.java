package org.jj;

public record Order (int orderId, double price, long quantity, long tradedQuantity) {}
