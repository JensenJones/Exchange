package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MatchingEngine {

    UUID createOrder(long quantity, long price, BuySell buySell);

    boolean cancelOrder(UUID id);
}
