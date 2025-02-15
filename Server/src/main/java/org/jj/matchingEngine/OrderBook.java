package org.jj.matchingEngine;

import org.jetbrains.annotations.Nullable;
import org.jj.BuySell;
import org.jj.Service;
import org.jj.providers.SystemTimestampProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OrderBook {
    private final OrderBookSide buySide = new OrderBookSide(new SystemTimestampProvider(), Comparator.reverseOrder());
    private final OrderBookSide sellSide = new OrderBookSide(new SystemTimestampProvider(), Comparator.naturalOrder());

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBook.class);
    private final OrderBookUpdateListenerImpl listener;

    public void addOrder(int id, BuySell buySell, long quantity, long price) {
        LOGGER.info("Adding order, Order id = {}", id);
        long quantityFilled = getOrderSide(BuySell.getOtherSide(buySell)).matchOrder(id, quantity, price);
        if (quantityFilled < quantity) {
            getOrderSide(buySell).addOrder(id, quantity, quantityFilled, price);
        }
        listener.onOrderBookUpdate(getOrderBook());
    }

    public OrderBook(OrderBookUpdateListenerImpl listener) {
        this.listener = listener;
    }

    boolean cancelOrder(int id) {
        BuySell buySell = getOrderSide(id);
        if (buySell == null) {
            return false;
        }
        OrderBookSide sameSideOrders = getOrderSide(buySell);
        return sameSideOrders.removeOrder(id);
    }

    Service.OrderBook getOrderBook() {
        LOGGER.info("Retrieving order book");
        AbstractMap.SimpleEntry<List<Long>, List<Long>> fiveBestBuyOrdersAndQuantitiesList = buySide.getFiveBestOrdersAndQuantitiesList();
        AbstractMap.SimpleEntry<List<Long>, List<Long>> fiveBestSellOrdersAndQuantitiesList = sellSide.getFiveBestOrdersAndQuantitiesList();

        List<Double> convertedBuyPrices = new ArrayList<>();
        for (long price : fiveBestBuyOrdersAndQuantitiesList.getKey()) {
            convertedBuyPrices.add((price / 1000.0));
        }

        List<Double> convertedSellPrices = new ArrayList<>();
        for (long price : fiveBestSellOrdersAndQuantitiesList.getKey()) {
            convertedSellPrices.add((price / 1000.0));
        }

        Service.OrderBook.Builder orderBookBuilder = Service.OrderBook.newBuilder();

        orderBookBuilder.addAllBuyQuantities(fiveBestBuyOrdersAndQuantitiesList.getValue());
        orderBookBuilder.addAllBuyPrices(convertedBuyPrices);
        orderBookBuilder.addAllSellQuantities(fiveBestSellOrdersAndQuantitiesList.getValue());
        orderBookBuilder.addAllSellPrices(convertedSellPrices);

        return orderBookBuilder.build();
    }

    public OrderBookUpdateListenerImpl getListener() {
        return listener;
    }

    private OrderBookSide getOrderSide(BuySell buySell) {
        if (buySell == BuySell.BUY) {
            return buySide;
        }
        return sellSide;
    }

    @Nullable
    private BuySell getOrderSide(int id) {
        if (buySide.hasOrder(id)) {
            return BuySell.BUY;
        } else if (sellSide.hasOrder(id)) {
            return BuySell.SELL;
        } else {
            return null;
        }
    }
}
