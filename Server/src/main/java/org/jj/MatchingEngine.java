package org.jj;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MatchingEngine {

    OrderState createOrder(long price, double quantity, BuySell buySell);

    @Nullable OrderState getOrder(UUID id);

    @Nullable OrderState cancelOrder(UUID id);
}
