package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MatchingEngine {

    Order createOrder(long price, long quantity, BuySell buySell);

    @Nullable Order getOrder(UUID id);

    @Nullable Order cancelOrder(UUID id);
}
