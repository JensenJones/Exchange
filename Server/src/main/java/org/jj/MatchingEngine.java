package org.jj;

import org.jetbrains.annotations.Nullable;

public interface MatchingEngine {
    Order createOrder();

    @Nullable
    Order getOrder(int id);

    @Nullable
    Order cancelOrder(int id);
}
